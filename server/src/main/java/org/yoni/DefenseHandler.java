package org.yoni;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DefenseHandler implements HttpHandler {
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private static final Map<String, RequestFrame> CLIENT_ID_TO_REQUEST_FRAMES =
      new ConcurrentHashMap<>();

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    String clientId = getClientId(httpExchange);

    incrementRequestsInFrame(clientId);

    if (validate(clientId)) {
      httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
    } else {
      httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAVAILABLE, -1);
    }

    httpExchange.close();
  }

  private static void incrementRequestsInFrame(String clientId) {
    RequestFrame requestFrame = CLIENT_ID_TO_REQUEST_FRAMES.get(clientId);

    if (requestFrame == null || requestFrame.isDeprecated()) {
      CLIENT_ID_TO_REQUEST_FRAMES.put(clientId, new RequestFrame());
      return;
    }

    requestFrame.incrementRequests();

    LOGGER.info(String.format("For clientId %s - reqeustFrame %s", clientId, requestFrame));
  }

  private static boolean validate(String clientId) {
    RequestFrame requestFrame = CLIENT_ID_TO_REQUEST_FRAMES.get(clientId);

    return requestFrame.isValid();
  }

  private static String getClientId(HttpExchange httpExchange) {
    return httpExchange.getRequestURI().getQuery().split("=")[1];
  }
}
