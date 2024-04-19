package org.yoni;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Main {
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private static final Scanner SCANNER = new Scanner(System.in);

  public static void main(String[] args) {
    LOGGER.info("Welcome to DoS attacking!");

    int numberOfClients = getNumberOfClients();

    ExecutorService executorService = Executors.newFixedThreadPool(numberOfClients);
    IntStream.range(0, numberOfClients).mapToObj(Attacker::new).forEach(executorService::execute);

    awaitUserTermination();

    try {
      executorService.shutdownNow();
      boolean terminatedSuccessfully = executorService.awaitTermination(5, TimeUnit.SECONDS);
      if (terminatedSuccessfully) {
        System.exit(0);
      }
    } catch (InterruptedException e) {
      LOGGER.log(Level.SEVERE, "Could not terminate gracefully", e);
      Thread.currentThread().interrupt();
      System.exit(-1);
    }
  }

  private static int getNumberOfClients() {
    int numberOfClients = 0;

    while (numberOfClients <= 0) {
      LOGGER.log(Level.INFO, "Enter number of attacking clients: ");
      try {
        numberOfClients = Integer.parseInt(SCANNER.nextLine());
        if (numberOfClients <= 0) {
          LOGGER.log(Level.INFO, "Please enter a positive integer");
        }
      } catch (NumberFormatException e) {
        LOGGER.log(Level.INFO, "Invalid input");
      }
    }

    return numberOfClients;
  }

  private static void awaitUserTermination() {
    LOGGER.log(Level.INFO, "Press Enter to stop all clients");

    SCANNER.nextLine();
  }
}
