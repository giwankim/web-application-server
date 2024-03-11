package com.giwankim.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
  private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

  private final Socket connection;

  public RequestHandler(Socket connectionSocket) {
    this.connection = connectionSocket;
  }

  @Override
  public void run() {
    logger.debug(
      "New Client Connection! Connected to IP: {}, Port: {}", connection.getInetAddress(), connection.getPort());

    try (
      InputStream in = connection.getInputStream();
      OutputStream out = connection.getOutputStream()
    ) {
      DataOutputStream dos = new DataOutputStream(out);
      byte[] body = "Hello, World!".getBytes();
      response200Header(dos, body.length);
      responseBody(dos, body);
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
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
