package com.giwankim.http;

public enum HttpStatus {
  OK(200, "OK"),
  CREATED(201, "CREATED"),
  NO_CONTENT(204, "No Content"),
  MOVED_PERMANENTLY(301, "Moved Permanently"),
  FOUND(302, "FOUND"),
  BAD_REQUEST(400, "Bad Request"),
  UNAUTHORIZED(401, "Unauthorized"),
  FORBIDDEN(403, "Forbidden"),
  NOT_FOUND(404, "Not Found"),
  INTERNAL_SERVER_ERROR(500, "Internal Server Error");

  private final int statusCode;
  private final String reason;

  HttpStatus(int statusCode, String reason) {
    this.statusCode = statusCode;
    this.reason = reason;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getReason() {
    return reason;
  }
}
