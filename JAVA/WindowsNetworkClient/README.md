# WindowsNetworkClient

## 機能

- WindowsProxy#getProxyでWindowsのプロキシを設定取得
- WindowsSSLContext#createSSLContextでWindowsの証明書取得

## 使用方法

### JDK 8

```java
HttpsURLConnection connection = (HttpsURLConnection) new URL("https://example.com").openConnection(WindowsProxy.getProxy());
connection.setSSLSocketFactory(WindowsSSLContext.createSSLContext().getSocketFactory());
```

### JDK 11

後で書く
