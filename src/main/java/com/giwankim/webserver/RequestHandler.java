package com.giwankim.webserver;

import com.giwankim.db.Database;
import com.giwankim.http.HttpCookies;
import com.giwankim.http.HttpRequest;
import com.giwankim.http.HttpRequestParser;
import com.giwankim.http.HttpResponse;
import com.giwankim.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class RequestHandler extends Thread {
  private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

  private static final String INDEX_HTML = "/index.html";

  private static final String LOGIN_HTML = "/user/login.html";

  private static final String LOGIN_FAILED_HTML = "/user/login_failed.html";

  private static final String LOGIN_COOKIE_KEY = "login";

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

      String path = getPathOrDefault(request.getPath());
      switch (path) {
        case "/user/create" -> createUser(request, response);
        case "/user/login" -> login(request, response);
        case "/user/list" -> listUsers(request, response);
        case null, default -> response.forward(path);
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private static void listUsers(HttpRequest request, HttpResponse response) {
    if (!isLoggedIn(request.getCookies())) {
      response.sendRedirect(LOGIN_HTML);
      return;
    }
    Collection<User> users = Database.findAll();
    StringBuilder sb = new StringBuilder();
    sb.append("<table border='1'>");
    for (User user : users) {
      sb.append("<tr>");
      sb.append("<td>").append(user.getUserId()).append("</td>");
      sb.append("<td>").append(user.getName()).append("</td>");
      sb.append("<td>").append(user.getEmail()).append("</td>");
      sb.append("</tr>");
    }
    sb.append("</table>");
    response.forwardBody(sb.toString());
  }

  private static void login(HttpRequest request, HttpResponse response) {
    Optional<User> maybeUser = Database.findUserById(request.getParameter("userId"));
    if (maybeUser.isPresent()) {
      User user = maybeUser.get();
      if (user.comparePasswords(request.getParameter("password"))) {
        response.setHeader("Set-Cookie", LOGIN_COOKIE_KEY + "=true; Path=/");
        response.sendRedirect(INDEX_HTML);
      } else {
        response.sendRedirect(LOGIN_FAILED_HTML);
      }
    } else {
      response.sendRedirect(LOGIN_FAILED_HTML);
    }
  }

  private static void createUser(HttpRequest request, HttpResponse response) {
    User user = new User(
      request.getParameter("userId"),
      request.getParameter("password"),
      request.getParameter("name"),
      request.getParameter("email"));
    Database.addUser(user);
    logger.debug("User : {}", user);
    response.sendRedirect(INDEX_HTML);
  }

  private static boolean isLoggedIn(HttpCookies cookies) {
    String value = cookies.getCookie(LOGIN_COOKIE_KEY);
    if (value == null) {
      return false;
    }
    return Boolean.parseBoolean(value);
  }

  private static String getPathOrDefault(String url) {
    if ("/".equals(url)) {
      return INDEX_HTML;
    }
    return url;
  }
}
