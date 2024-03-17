package com.giwankim.controller;

import com.giwankim.db.Database;
import com.giwankim.http.HttpRequest;
import com.giwankim.http.HttpResponse;
import com.giwankim.model.User;

import java.util.Optional;

import static com.giwankim.webserver.Constants.*;

public class LoginController extends AbstractController {

  @Override
  protected void doPost(HttpRequest request, HttpResponse response) {
    Optional<User> maybeUser = Database.findUserById(request.getParameter("userId"));
    if (maybeUser.isPresent()) {
      User user = maybeUser.get();
      if (user.comparePasswords(request.getParameter("password"))) {
        response.setHeader("Set-Cookie", COOKIE_LOGIN + "=true; Path=/");
        response.sendRedirect(INDEX);
      } else {
        response.sendRedirect(LOGIN_FAILED);
      }
    } else {
      response.sendRedirect(LOGIN_FAILED);
    }
  }
}
