import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class WindowsSSLContext {
	static {
		try {
			System.setProperty("com.sun.net.ssl.checkRevocation", "true");
			System.setProperty("com.sun.security.enableCRLDP", "true");
			Security.setProperty("ocsp.enable", "true");
		} catch(SecurityException e) {
			// セキュリティマネージャーへの設定権がない場合は無視する
		}
	}

	public static SSLContext createSSLContext() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException{
		KeyStore keyStore = KeyStore.getInstance("Windows-ROOT");
		keyStore.load(null, null);
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(keyStore);
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
		return sslContext;
	}
}
