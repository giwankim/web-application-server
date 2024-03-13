package com.giwankim.webserver;

import com.giwankim.db.Database;
import com.giwankim.model.User;
import com.giwankim.util.HttpRequestUtils;
import com.giwankim.util.IOUtils;
import com.giwankim.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestHandler extends Thread {
  private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

  private static final String WEBAPP_PATH = "src/main/webapp";
  private static final String CRLF = "\r\n";
  private static final String INDEX_HTML = "/index.html";
  private static final String LOGIN_FAILED_HTML = "/user/login_failed.html";

  private final Socket connection;

  public RequestHandler(Socket connectionSocket) {
    this.connection = Objects.requireNonNull(connectionSocket, "Connection socket must not be null");
  }

  @Override
  public void run() {
    logger.debug(
      "New Client Connection! Connected to IP: {}, Port: {}", connection.getInetAddress(), connection.getPort());

    try (
      InputStream in = connection.getInputStream();
      OutputStream out = connection.getOutputStream()
    ) {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, UTF_8));

      // request line
      String line = bufferedReader.readLine();
      if (StringUtils.isNullOrBlank(line)) {
        return;
      }
      logger.debug("request line : {}", line);
      List<String> requestLineParts = Arrays.asList(line.split("\\s+", 3));
      if (requestLineParts.size() < 3) {
        return;
      }

      // request headers
      HttpHeaders httpHeaders = new HttpHeaders();
      while ((line = bufferedReader.readLine()) != null) {
        if (line.isBlank()) {
          break;
        }
        httpHeaders.add(line);
      }
      int contentLength = httpHeaders.getContentLength();

      // response
      String url = getUrlOrDefault(requestLineParts);
      if (url.startsWith("/user/create")) {
        String body = IOUtils.readData(bufferedReader, contentLength);
        Map<String, String> params = HttpRequestUtils.parseQueryString(body);
        User user = new User(
          params.get("userId"),
          params.get("password"),
          params.get("name"),
          params.get("email"));
        Database.addUser(user);
        logger.debug("User: {}", user);
        DataOutputStream dos = new DataOutputStream(out);
        response302Header(dos, INDEX_HTML);
      } else if (url.equals("/user/login")) {
        String body = IOUtils.readData(bufferedReader, contentLength);
        Map<String, String> params = HttpRequestUtils.parseQueryString(body);
        Optional<User> optionalUser = Database.findUserById(params.get("userId"));
        if (optionalUser.isPresent()) {
          User user = optionalUser.get();
          if (user.comparePasswords(params.get("password"))) {
            DataOutputStream dos = new DataOutputStream(out);
            response302LoginSuccessHeader(dos);
          } else {
            responseResource(out, url);
          }
        } else {
          responseResource(out, LOGIN_FAILED_HTML);
        }
      } else {
        responseResource(out, url);
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private void responseResource(OutputStream out, String url) throws IOException {
    DataOutputStream dos = new DataOutputStream(out);
    byte[] body = Files.readAllBytes(Paths.get(WEBAPP_PATH, url));
    response200Header(dos, body.length);
    responseBody(dos, body);
  }

  private static String getUrlOrDefault(List<String> tokens) {
    String url = tokens.get(1);
    if ("/".equals(url)) {
      return INDEX_HTML;
    }
    return url;
  }

  private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
    try {
      dos.writeBytes("HTTP/1.1 200 OK" + CRLF);
      dos.writeBytes("Content-Type: text/html;charset=utf-8" + CRLF);
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
      dos.writeBytes("Set-Cookie: login=true" + CRLF);
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
