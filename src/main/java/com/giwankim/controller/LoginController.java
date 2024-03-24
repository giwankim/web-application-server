package com.giwankim.controller;

import com.giwankim.db.Database;
import com.giwankim.http.HttpRequest;
import com.giwankim.http.HttpResponse;
import com.giwankim.http.HttpSession;
import com.giwankim.model.User;

import java.util.Optional;

import static com.giwankim.webserver.Constants.*;

public class LoginController extends AbstractController {

  @Override
  protected void doPost(HttpRequest request, HttpResponse response) {
    Optional<User> optionalUser = Database.findUserById(request.getParameter("userId"));
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (user.comparePasswords(request.getParameter("password"))) {
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_USER_KEY, user);
        response.sendRedirect(INDEX_PAGE);
      } else {
        response.sendRedirect(LOGIN_FAILED_PAGE);
      }
    } else {
      response.sendRedirect(LOGIN_FAILED_PAGE);
    }
  }
}
