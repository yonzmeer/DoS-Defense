package org.yoni;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefenseHandler implements HttpHandler {
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    LOGGER.log(Level.INFO,"Handling request in {0}", Thread.currentThread().getName());
    String clientId = getClientId(httpExchange);

    if (validate(clientId))  {
      httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
    } else {
      httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAVAILABLE, -1);
    }

    httpExchange.close();
  }

  private boolean validate(String clientId) {
    return false;
  }

  private String getClientId(HttpExchange httpExchange) {
   return httpExchange.getRequestURI().getQuery().split("=")[1];
  }
}
