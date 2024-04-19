package org.yoni;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.HttpURLConnection;

public class DefenseHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
    exchange.close();
  }
}
