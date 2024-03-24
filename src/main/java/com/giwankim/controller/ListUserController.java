package com.giwankim.controller;

import com.giwankim.db.Database;
import com.giwankim.http.HttpRequest;
import com.giwankim.http.HttpResponse;
import com.giwankim.http.HttpSession;
import com.giwankim.model.User;

import java.util.Collection;

import static com.giwankim.webserver.Constants.LOGIN_PAGE;
import static com.giwankim.webserver.Constants.SESSION_USER_KEY;

public class ListUserController extends AbstractController {

  @Override
  protected void doGet(HttpRequest request, HttpResponse response) {
    if (!isLoggedIn(request.getSession())) {
      response.sendRedirect(LOGIN_PAGE);
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

  private static boolean isLoggedIn(HttpSession session) {
    return session.getAttribute(SESSION_USER_KEY) != null;
  }
}
