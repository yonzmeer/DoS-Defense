package org.yoni;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private static final Scanner SCANNER = new Scanner(System.in);

  public static void main(String[] args) throws IOException {
    LOGGER.info("Starting server");

    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/", new DefenseHandler());
    server.setExecutor(Executors.newCachedThreadPool());

    server.start();

    awaitUserTermination();

    server.stop(0);
    System.exit(0);
  }

  private static void awaitUserTermination() {
    LOGGER.log(Level.INFO, "Press Enter to stop server");

    SCANNER.nextLine();
  }
}
