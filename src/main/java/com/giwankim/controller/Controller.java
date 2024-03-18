package com.giwankim.controller;

import com.giwankim.http.HttpRequest;
import com.giwankim.http.HttpResponse;

public interface Controller {
  void service(HttpRequest request, HttpResponse response);
}
