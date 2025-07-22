import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.net.ssl.SSLContext;

public class Main {
	public static void main(String[] args) throws Exception {
		SSLContext sslContext = WindowsSSLContext.createSSLContext();
		HttpClient client = HttpClient.newBuilder()
				.sslContext(sslContext)
				.build();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI("https://localhost"))
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		response.sslSession().ifPresent((session) -> {
			System.out.println("TLS: " + session.getProtocol());
			System.out.println("スイート: " + session.getCipherSuite());
		});
		System.out.println(response.body());
	}
}
