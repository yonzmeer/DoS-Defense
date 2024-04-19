package org.yoni;

import java.net.http.HttpClient;
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
        LOGGER.log(Level.INFO, "Running something with client {0}", clientId);

        waitUntilNextRequest();
      } catch (InterruptedException e) {
        LOGGER.log(Level.INFO, "Client {0} was interrupted", clientId);
        Thread.currentThread().interrupt();
      }
    }
  }

  private void waitUntilNextRequest() throws InterruptedException {
    Thread.sleep(RANDOM.nextInt(0, 5) * 1000L);
  }
}
