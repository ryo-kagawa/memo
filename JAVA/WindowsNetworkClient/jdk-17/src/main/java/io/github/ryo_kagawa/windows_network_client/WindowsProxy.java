package io.github.ryo_kagawa.windows_network_client;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Optional;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

import io.github.ryo_kagawa.windows_network_client.win32.DWORDUtil;
import io.github.ryo_kagawa.windows_network_client.win32.WinHttp;

public class WindowsProxy {
	private WindowsProxy() {}

	public static Proxy getProxy(String targetUrl) throws Win32Exception {
		var proxyConfig = new WinHttp.WINHTTP_CURRENT_USER_IE_PROXY_CONFIG.ByReference();
		if (!WinHttp.INSTANCE.WinHttpGetIEProxyConfigForCurrentUser(proxyConfig).booleanValue()) {
			throw new Win32Exception(Native.getLastError());
		}
		if (proxyConfig.fAutoDetect.booleanValue()) {
			return wpad(targetUrl);
		}
		if (Optional.ofNullable(proxyConfig.lpszAutoConfigUrl.getValue()).isPresent()) {
			return pac(targetUrl, proxyConfig);
		}
		if (Optional.ofNullable(proxyConfig.lpszProxy.getValue()).isPresent()) {
			return manual(targetUrl, proxyConfig);
		}
		return Proxy.NO_PROXY;
	}

	private static Proxy parseProxy(String value) {
		String[] parts = value.split(":");
		String host = parts[0];
		int port = parts.length >= 2 ? Integer.parseInt(parts[1]) : 80;
		return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
	}

	private static Proxy wpad(String targetUrl) throws Win32Exception {
		WinNT.HANDLE hSession = WinHttp.INSTANCE.WinHttpOpen(null, WinHttp.WINHTTP_ACCESS_TYPE_NO_PROXY, null, null, new WinDef.DWORD());
		if (hSession == null) {
			throw new Win32Exception(Native.getLastError());
		}
		try {
			var autoProxyOptions = new WinHttp.WINHTTP_AUTOPROXY_OPTIONS.ByReference();
			autoProxyOptions.fAutoLogonIfChallenged = new WinDef.BOOL(true);
			autoProxyOptions.dwFlags = WinHttp.WINHTTP_AUTOPROXY_AUTO_DETECT;
			autoProxyOptions.dwAutoDetectFlags = DWORDUtil.Operator.or(WinHttp.WINHTTP_AUTO_DETECT_TYPE_DHCP, WinHttp.WINHTTP_AUTO_DETECT_TYPE_DNS_A);
			var proxyInfo = new WinHttp.WINHTTP_PROXY_INFO.ByReference();
			if (!WinHttp.INSTANCE.WinHttpGetProxyForUrl(hSession, new WTypes.LPWSTR(targetUrl), autoProxyOptions, proxyInfo).booleanValue()) {
				throw new Win32Exception(Native.getLastError());
			}
			return Optional.ofNullable(proxyInfo.lpszProxy).map((lpszProxy) -> lpszProxy.getValue()).filter((lpszProxy) -> !lpszProxy.isEmpty()).map((lpszProxy) -> parseProxy(lpszProxy)).orElse(Proxy.NO_PROXY);
		} finally {
			WinHttp.INSTANCE.WinHttpCloseHandle(hSession);
		}
	}

	private static Proxy pac(String targetUrl, WinHttp.WINHTTP_CURRENT_USER_IE_PROXY_CONFIG.ByReference proxyConfig) throws Win32Exception {
		WinNT.HANDLE hSession = WinHttp.INSTANCE.WinHttpOpen(null, WinHttp.WINHTTP_ACCESS_TYPE_NO_PROXY, null, null, new WinDef.DWORD());
		if (hSession == null) {
			throw new Win32Exception(Native.getLastError());
		}
		try {
			var autoProxyOptions = new WinHttp.WINHTTP_AUTOPROXY_OPTIONS.ByReference();
			autoProxyOptions.fAutoLogonIfChallenged = new WinDef.BOOL(true);
			autoProxyOptions.dwFlags = WinHttp.WINHTTP_AUTOPROXY_CONFIG_URL;
			autoProxyOptions.lpszAutoConfigUrl = proxyConfig.lpszAutoConfigUrl;
			var proxyInfo = new WinHttp.WINHTTP_PROXY_INFO.ByReference();
			if (!WinHttp.INSTANCE.WinHttpGetProxyForUrl(hSession, new WTypes.LPWSTR(targetUrl), autoProxyOptions, proxyInfo).booleanValue()) {
				throw new Win32Exception(Native.getLastError());
			}
			return Optional.ofNullable(proxyInfo.lpszProxy).map((lpszProxy) -> lpszProxy.getValue()).filter((lpszProxy) -> !lpszProxy.isEmpty()).map((lpszProxy) -> parseProxy(lpszProxy)).orElse(Proxy.NO_PROXY);
		} finally {
			WinHttp.INSTANCE.WinHttpCloseHandle(hSession);
		}
	}

	private static Proxy manual(String targetUrl, WinHttp.WINHTTP_CURRENT_USER_IE_PROXY_CONFIG.ByReference proxyConfig) {
		Proxy proxy = Optional.ofNullable(proxyConfig.lpszProxy).map((lpszProxy) -> lpszProxy.getValue()).filter((lpszProxy) -> !lpszProxy.isEmpty()).map((lpszProxy) -> parseProxy(lpszProxy)).orElse(Proxy.NO_PROXY);
		if (proxy.equals(Proxy.NO_PROXY)) {
			return proxy;
		}
		return Optional.ofNullable(proxyConfig.lpszProxyBypass).map((lpszProxyBypass) -> lpszProxyBypass.getValue()).filter((lpszProxyBypass) -> !lpszProxyBypass.isEmpty()).filter((lpszProxyBypass) -> !Arrays.stream(lpszProxyBypass.split(";")).map(String::trim).anyMatch((bypass) -> {
			if (bypass.equalsIgnoreCase("<local>") && !targetUrl.contains(".")) {
				return true;
			}
			return targetUrl.matches(bypass.replace(".", "\\.").replace("*", ".*"));
		})).map((x) -> proxy).orElse(Proxy.NO_PROXY);
	}
}
