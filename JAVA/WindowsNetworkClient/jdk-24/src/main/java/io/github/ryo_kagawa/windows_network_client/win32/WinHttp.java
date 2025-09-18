package io.github.ryo_kagawa.windows_network_client.win32;

import java.util.Optional;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;

public interface WinHttp extends StdCallLibrary {
	public static final WinHttp INSTANCE = Native.load("winhttp", WinHttp.class);

	public static final WinDef.DWORD WINHTTP_ACCESS_TYPE_NO_PROXY = new WinDef.DWORD(1); 
	public static final WinDef.DWORD WINHTTP_AUTO_DETECT_TYPE_DHCP = new WinDef.DWORD(0x00000001);
	public static final WinDef.DWORD WINHTTP_AUTO_DETECT_TYPE_DNS_A = new WinDef.DWORD(0x00000002);
	public static final WinDef.DWORD WINHTTP_AUTOPROXY_AUTO_DETECT = new WinDef.DWORD(0x00000001);
	public static final WinDef.DWORD WINHTTP_AUTOPROXY_CONFIG_URL = new WinDef.DWORD(0x00000002);

	/**
	 * typedef struct _WINHTTP_AUTOPROXY_OPTIONS {
	 *   DWORD   dwFlags;
	 *   DWORD   dwAutoDetectFlags;
	 *   LPCWSTR lpszAutoConfigUrl;
	 *   LPVOID  lpvReserved;
	 *   DWORD   dwReserved;
	 *   BOOL    fAutoLogonIfChallenged;
	 * } WINHTTP_AUTOPROXY_OPTIONS, *PWINHTTP_AUTOPROXY_OPTIONS;
	 */
	@Structure.FieldOrder({"dwFlags", "dwAutoDetectFlags", "lpszAutoConfigUrl", "lpvReserved", "dwReserved", "fAutoLogonIfChallenged"})
	public static class WINHTTP_AUTOPROXY_OPTIONS extends Structure {
		public static class ByReference extends WINHTTP_AUTOPROXY_OPTIONS implements Structure.ByReference {}

		public WinDef.DWORD dwFlags;
		public WinDef.DWORD dwAutoDetectFlags;
		public WTypes.LPWSTR lpszAutoConfigUrl;
		public WinDef.LPVOID lpvReserved;
		public WinDef.DWORD dwReserved;
		public WinDef.BOOL fAutoLogonIfChallenged;
	}

	/**
	 * typedef struct _WINHTTP_CURRENT_USER_IE_PROXY_CONFIG {
     *   BOOL   fAutoDetect;
     *   LPWSTR lpszAutoConfigUrl;
     *   LPWSTR lpszProxy;
     *   LPWSTR lpszProxyBypass;
     * } WINHTTP_CURRENT_USER_IE_PROXY_CONFIG, *PWINHTTP_CURRENT_USER_IE_PROXY_CONFIG;
	 */
	@Structure.FieldOrder({"fAutoDetect", "lpszAutoConfigUrl", "lpszProxy", "lpszProxyBypass"})
	public static class WINHTTP_CURRENT_USER_IE_PROXY_CONFIG extends Structure {
		public static class ByReference extends WINHTTP_CURRENT_USER_IE_PROXY_CONFIG implements Structure.ByReference, AutoCloseable {
			@Override
			public void close() {
				Optional.ofNullable(lpszAutoConfigUrl).ifPresent((lpszAutoConfigUrl) -> Kernel32.INSTANCE.GlobalFree(lpszAutoConfigUrl.getPointer()));
				Optional.ofNullable(lpszProxy).ifPresent((lpszProxy) -> Kernel32.INSTANCE.GlobalFree(lpszProxy.getPointer()));
				Optional.ofNullable(lpszProxyBypass).ifPresent((lpszProxyBypass) -> Kernel32.INSTANCE.GlobalFree(lpszProxyBypass.getPointer()));
			}
		}

		public WinDef.BOOL fAutoDetect;
		public WTypes.LPWSTR lpszAutoConfigUrl;
		public WTypes.LPWSTR lpszProxy;
		public WTypes.LPWSTR lpszProxyBypass;
	}

	/**
	 * typedef struct _WINHTTP_PROXY_INFO {
	 *   DWORD  dwAccessType;
	 *   LPWSTR lpszProxy;
	 *   LPWSTR lpszProxyBypass;
	 * } WINHTTP_PROXY_INFO, *LPWINHTTP_PROXY_INFO, *PWINHTTP_PROXY_INFO;
	 */
	@Structure.FieldOrder({"dwAccessType", "lpszProxy", "lpszProxyBypass"})
	public static class WINHTTP_PROXY_INFO extends Structure {
	    public static class ByReference extends WINHTTP_PROXY_INFO implements Structure.ByReference, AutoCloseable {
			@Override
			public void close() {
				Optional.ofNullable(lpszProxy).ifPresent((lpszProxy) -> Kernel32.INSTANCE.GlobalFree(lpszProxy.getPointer()));
				Optional.ofNullable(lpszProxyBypass).ifPresent((lpszProxyBypass) -> Kernel32.INSTANCE.GlobalFree(lpszProxyBypass.getPointer()));
			}
	    }

	    public WinDef.DWORD dwAccessType;
	    public WTypes.LPWSTR lpszProxy;
	    public WTypes.LPWSTR lpszProxyBypass;
	}

    /**
     * WINHTTPAPI BOOL WinHttpCloseHandle(
     *   [in] HINTERNET hInternet
     * );
     * @param hInternet
     * @return
     */
    public WinDef.BOOL WinHttpCloseHandle(WinNT.HANDLE hInternet);

	/**
	 * WINHTTPAPI HINTERNET WinHttpOpen(
	 *   [in, optional] LPCWSTR pszAgentW,
	 *   [in]           DWORD   dwAccessType,
	 *   [in]           LPCWSTR pszProxyW,
	 *   [in]           LPCWSTR pszProxyBypassW,
	 *   [in]           DWORD   dwFlags
	 * );
	 * @param userAgent
	 * @param accessType
	 * @param proxyName
	 * @param proxyBypass
	 * @param flags
	 * @return
	 */
    public WinNT.HANDLE WinHttpOpen(WTypes.LPWSTR userAgent, WinDef.DWORD accessType, WTypes.LPWSTR proxyName, WTypes.LPWSTR proxyBypass, WinDef.DWORD flags);

	/**
	 * WINHTTPAPI BOOL WinHttpGetIEProxyConfigForCurrentUser(
     *   [in, out] WINHTTP_CURRENT_USER_IE_PROXY_CONFIG *pProxyConfig
     * );
	 * @param config
	 * @return
	 */
    public WinDef.BOOL WinHttpGetIEProxyConfigForCurrentUser(WINHTTP_CURRENT_USER_IE_PROXY_CONFIG.ByReference pProxyConfig);

    /**
     * WINHTTPAPI BOOL WinHttpGetProxyForUrl(
     *   [in]  HINTERNET                 hSession,
     *   [in]  LPCWSTR                   lpcwszUrl,
     *   [in]  WINHTTP_AUTOPROXY_OPTIONS *pAutoProxyOptions,
     *   [out] WINHTTP_PROXY_INFO        *pProxyInfo
     * );
     * @param hSession
     * @param url
     * @param pAutoProxyOptions
     * @param pProxyInfo
     * @return
     */
    public WinDef.BOOL WinHttpGetProxyForUrl(WinNT.HANDLE hSession, WTypes.LPWSTR url, WINHTTP_AUTOPROXY_OPTIONS.ByReference pAutoProxyOptions, WINHTTP_PROXY_INFO.ByReference pProxyInfo);
}
