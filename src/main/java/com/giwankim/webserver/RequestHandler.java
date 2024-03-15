package com.giwankim.webserver;

import com.giwankim.db.Database;
import com.giwankim.http.HttpCookies;
import com.giwankim.http.HttpRequest;
import com.giwankim.http.HttpRequestParser;
import com.giwankim.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class RequestHandler extends Thread {
  private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

  private static final String WEBAPP_PATH = "src/main/webapp";

  private static final String CRLF = "\r\n";

  private static final String INDEX_HTML = "/index.html";

  private static final String LOGIN_HTML = "/user/login.html";

  private static final String LOGIN_FAILED_HTML = "/user/login_failed.html";

  private static final String LOGIN_COOKIE_KEY = "login";

  private final Socket connection;

  public RequestHandler(Socket connectionSocket) {
    this.connection = Objects.requireNonNull(connectionSocket, "Connection socket must not be null");
  }

  @Override
  public void run() {
    logger.debug("New Client Connection! Connected to IP: {}, Port: {}", connection.getInetAddress(), connection.getPort());

    try (
      connection;
      InputStream in = connection.getInputStream();
      OutputStream out = connection.getOutputStream()) {
      HttpRequest request = HttpRequestParser.parse(in);
      String path = getPathOrDefault(request.getPath());

      if ("/user/create".equals(path)) {
        User user = new User(
          request.getParameter("userId"),
          request.getParameter("password"),
          request.getParameter("name"),
          request.getParameter("email"));
        Database.addUser(user);
        logger.debug("User : {}", user);
        DataOutputStream dos = new DataOutputStream(out);
        response302Header(dos, INDEX_HTML);
      } else if ("/user/login".equals(path)) {
        String userId = request.getParameter("userId");
        Optional<User> maybeUser = Database.findUserById(userId);
        if (maybeUser.isPresent()) {
          User user = maybeUser.get();
          if (user.comparePasswords(request.getParameter("password"))) {
            DataOutputStream dos = new DataOutputStream(out);
            response302LoginSuccessHeader(dos);
          } else {
            responseResource(out, path);
          }
        } else {
          responseResource(out, LOGIN_FAILED_HTML);
        }
      } else if ("/user/list".equals(path)) {
        if (!isLoggedIn(request)) {
          responseResource(out, LOGIN_HTML);
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
        byte[] body = sb.toString().getBytes();
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length);
        responseBody(dos, body);
      } else {
        responseResource(out, path);
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private static boolean isLoggedIn(HttpRequest request) {
    HttpCookies cookies = request.getCookies();
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

  private void responseResource(OutputStream out, String url) throws IOException {
    DataOutputStream dos = new DataOutputStream(out);
    byte[] body = Files.readAllBytes(Paths.get(WEBAPP_PATH, url));
    response200Header(dos, body.length);
    responseBody(dos, body);
  }

  private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
    try {
      dos.writeBytes("HTTP/1.1 200 OK" + CRLF);
      dos.writeBytes("Content-Type: text/html; charset=utf-8" + CRLF);
      dos.writeBytes("Content-Length: " + lengthOfBodyContent + CRLF);
      dos.writeBytes(CRLF);
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private void response302Header(DataOutputStream dos, String location) {
    try {
      dos.writeBytes("HTTP/1.1 302 Found" + CRLF);
      dos.writeBytes("Location: " + location + CRLF);
      dos.writeBytes(CRLF);
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private void response302LoginSuccessHeader(DataOutputStream dos) {
    try {
      dos.writeBytes("HTTP/1.1 302 Found" + CRLF);
      dos.writeBytes(String.format("Set-Cookie: %s=true%s", LOGIN_COOKIE_KEY, CRLF));
      dos.writeBytes("Location: " + INDEX_HTML + CRLF);
      dos.writeBytes(CRLF);
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private void responseBody(DataOutputStream dos, byte[] body) {
    try {
      dos.write(body, 0, body.length);
      dos.writeBytes(CRLF);
      dos.flush();
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }
}
