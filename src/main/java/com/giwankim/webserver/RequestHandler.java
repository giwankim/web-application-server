package com.giwankim.webserver;

import com.giwankim.controller.Controller;
import com.giwankim.http.HttpRequest;
import com.giwankim.http.HttpRequestParser;
import com.giwankim.http.HttpResponse;
import com.giwankim.http.HttpSessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

import static com.giwankim.webserver.Constants.INDEX;

public class RequestHandler extends Thread {
  private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

  private final Socket client;

  public RequestHandler(Socket client) {
    this.client = Objects.requireNonNull(client, "Connection socket must not be null");
  }

  @Override
  public void run() {
    logger.debug("New Client Connection! Connected to IP: {}, Port: {}", client.getInetAddress(), client.getPort());

    try (
      client;
      BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
      DataOutputStream writer = new DataOutputStream(client.getOutputStream())
    ) {

      HttpRequest request = HttpRequestParser.parse(reader);
      HttpResponse response = HttpResponse.from(writer);

      if (!request.getCookies().containsCookie(HttpSessions.SESSION_ID_NAME)) {
        response.setCookie(HttpSessions.SESSION_ID_NAME, String.valueOf(UUID.randomUUID()));
      }

      Controller controller = RequestMapping.getController(request.getPath());
      if (controller == null) {
        response.forward(getPathOrDefault(request.getPath()));
      } else {
        controller.service(request, response);
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private static String getPathOrDefault(String url) {
    if ("/".equals(url)) {
      return INDEX;
    }
    return url;
  }
}
