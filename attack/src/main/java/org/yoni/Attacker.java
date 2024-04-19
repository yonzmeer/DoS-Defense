package org.yoni;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.yoni.Constants.URL;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
        request();

        waitUntilNextRequest();
      } catch (InterruptedException e) {
        LOGGER.log(Level.INFO, "Client {0} was interrupted", clientId);
        Thread.currentThread().interrupt();
      }
    }
  }

  private void request() throws InterruptedException {
    String url = String.format("%s?clientId=%s", URL, clientId);
    URI uri = URI.create(url);

    HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

    try {
      HttpResponse<String> response = httpClient.send(request, ofString());

      LOGGER.log(Level.INFO, "Response code was {0}", response.statusCode());
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not perform http request with client", e);
    }
  }

  private void waitUntilNextRequest() throws InterruptedException {
    Thread.sleep(RANDOM.nextInt(0, 5) * 1000L);
  }
}
