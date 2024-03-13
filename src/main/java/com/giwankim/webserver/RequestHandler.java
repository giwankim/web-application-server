package com.giwankim.webserver;

import com.giwankim.model.User;
import com.giwankim.util.HttpRequestUtils;
import com.giwankim.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestHandler extends Thread {
  private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
  private static final String WEBAPP_PATH = "src/main/webapp";
  private static final String INDEX_HTML = "/index.html";

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

      List<String> tokens = Arrays.asList(line.split("\\s+", 3));
      if (tokens.size() != 3) {
        return;
      }

      // request headers
      while ((line = bufferedReader.readLine()) != null) {
        if (line.isBlank()) {
          break;
        }
        logger.debug("header : {}", line);
      }

      // response
      String url = getUrlOrDefault(tokens);
      if (url.startsWith("/user/create")) {
        // query string
        String queryString = "";
        if (url.contains("?")) {
          queryString = url.substring(url.indexOf("?") + 1);
        }
        Map<String, String> queryParams = HttpRequestUtils.parseQueryString(queryString);
        // new user
        User user = new User(
          queryParams.get("userId"),
          queryParams.get("password"),
          queryParams.get("name"),
          queryParams.get("email"));
        logger.debug("User: {}", user);
      } else {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(Paths.get(WEBAPP_PATH, url));
        response200Header(dos, body.length);
        responseBody(dos, body);
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
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
      dos.writeBytes("HTTP/1.1 200 OK \r\n");
      dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
      dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
      dos.writeBytes("\r\n");
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private void responseBody(DataOutputStream dos, byte[] body) {
    try {
      dos.write(body, 0, body.length);
      dos.flush();
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }
}
