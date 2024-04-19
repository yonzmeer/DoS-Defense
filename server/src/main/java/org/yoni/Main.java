package org.yoni;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public static void main(String[] args) throws IOException {
    LOGGER.info("Starting server");

    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/", new DefenseHandler());
    server.setExecutor(Executors.newCachedThreadPool());

    server.start();
  }
}