package au.com.example.user;

import au.com.example.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerServerTest {

    @LocalServerPort
    private int port;

    private final HttpClient client = HttpClient.newHttpClient();

    private URI uri(String path) {
        return URI.create("http://localhost:" + port + "/api" + path);
    }

    @Test
    public void shouldRejectAnonymousRequestWithUnauthorized() throws Exception {
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(uri("/user")).GET().build(), HttpResponse.BodyHandlers.ofString());

        assertEquals(401, response.statusCode());
    }

    @Test
    public void shouldLoginAndUseTokenForSubsequentRequests() throws Exception {
        String basic = "Basic " + Base64.getEncoder().encodeToString("user@tester.com.au:password".getBytes());
        HttpResponse<String> login = client.send(
                HttpRequest.newBuilder(uri("/auth/login")).header("Authorization", basic)
                        .POST(HttpRequest.BodyPublishers.noBody()).build(),
                HttpResponse.BodyHandlers.ofString());

        assertEquals(200, login.statusCode());
        String token = login.headers().firstValue("X-AUTH-TOKEN").orElse(null);
        assertNotNull(token);

        HttpResponse<String> user = client.send(
                HttpRequest.newBuilder(uri("/user")).header("X-AUTH-TOKEN", token).GET().build(),
                HttpResponse.BodyHandlers.ofString());

        assertEquals(200, user.statusCode());
        assertTrue(user.body().contains("\"email\":\"user@tester.com.au\""));
    }
}
