package com.giwankim.controller;

import com.giwankim.db.Database;
import com.giwankim.http.HttpCookies;
import com.giwankim.http.HttpRequest;
import com.giwankim.http.HttpResponse;
import com.giwankim.model.User;

import java.util.Collection;

import static com.giwankim.webserver.Constants.COOKIE_LOGIN;
import static com.giwankim.webserver.Constants.LOGIN;

public class ListUserController implements Controller {

  @Override
  public void service(HttpRequest request, HttpResponse response) {
    if (!isLoggedIn(request.getCookies())) {
      response.sendRedirect(LOGIN);
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

  private static boolean isLoggedIn(HttpCookies cookies) {
    String value = cookies.getCookie(COOKIE_LOGIN);
    if (value == null) {
      return false;
    }
    return Boolean.parseBoolean(value);
  }
}
