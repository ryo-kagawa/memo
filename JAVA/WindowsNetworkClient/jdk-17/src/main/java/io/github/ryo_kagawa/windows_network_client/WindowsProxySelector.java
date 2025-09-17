package io.github.ryo_kagawa.windows_network_client;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;

public class WindowsProxySelector extends ProxySelector {
	public static final SelectExceptionHandler defaultSelectExceptionHandler = (uri, exception) -> Collections.emptyList();
	public static final ConnectFailedHandler defaultConnectFailedHandler = (uri, sa, ioe) -> {};
	private final SelectExceptionHandler selectExceptionHandler;
	private final ConnectFailedHandler connectFailedHandler;

	@FunctionalInterface
	public static interface SelectExceptionHandler {
		List<Proxy> apply(URI uri, Exception e);
	}

	@FunctionalInterface
	public static interface ConnectFailedHandler {
		void accept(URI uri, SocketAddress sa, IOException ioe);
	}

	public WindowsProxySelector() {
		this.selectExceptionHandler = defaultSelectExceptionHandler;
		this.connectFailedHandler = defaultConnectFailedHandler;
	}

	public WindowsProxySelector(SelectExceptionHandler selectExceptionHandler, ConnectFailedHandler connectFailedHandler) {
		this.selectExceptionHandler = selectExceptionHandler;
		this.connectFailedHandler = connectFailedHandler;
	}

	@Override
	public List<Proxy> select(URI uri) {
		try {
			return List.of(WindowsProxy.getProxy(uri.toURL().toString()));
		} catch (Exception e) {
			return selectExceptionHandler.apply(uri, e);
		}
	}

	@Override
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		connectFailedHandler.accept(uri, sa, ioe);
	}
}
