package org.yoni;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.yoni.Constants.URL;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Attacker implements Runnable {
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private static final Random RANDOM = new Random();

  private final int clientId;

  private final HttpClient httpClient = HttpClient.newHttpClient();

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        sendRequest();

        waitUntilNextRequest();
      } catch (InterruptedException e) {
        LOGGER.info(String.format("ClientId=%d was interrupted", clientId));

        Thread.currentThread().interrupt();
      }
    }
  }

  private void sendRequest() {
    HttpRequest httpRequest = createRequest();

    httpClient
        .sendAsync(httpRequest, ofString())
        .thenAccept(
            response ->
                LOGGER.info(
                    String.format(
                        "ClientId=%d, Response code was %d", clientId, response.statusCode())))
        .exceptionally(
            e -> {
              LOGGER.log(
                  Level.SEVERE,
                  String.format("Could not perform http request with clientId=%d", clientId),
                  e);
              return null;
            });
  }

  private HttpRequest createRequest() {
    String url = String.format("%s?clientId=%s", URL, clientId);
    URI uri = URI.create(url);

    return HttpRequest.newBuilder().uri(uri).build();
  }

  private void waitUntilNextRequest() throws InterruptedException {
    Thread.sleep(RANDOM.nextLong(0, 1500L));
  }
}
