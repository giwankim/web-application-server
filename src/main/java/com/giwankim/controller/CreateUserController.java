package com.giwankim.controller;

import com.giwankim.db.Database;
import com.giwankim.http.HttpRequest;
import com.giwankim.http.HttpResponse;
import com.giwankim.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.giwankim.webserver.Constants.INDEX_PAGE;

public class CreateUserController extends AbstractController {
  private static final Logger logger = LoggerFactory.getLogger(CreateUserController.class);

  @Override
  protected void doPost(HttpRequest request, HttpResponse response) {
    User user = new User(
      request.getParameter("userId"),
      request.getParameter("password"),
      request.getParameter("name"),
      request.getParameter("email"));
    Database.addUser(user);
    logger.debug("User : {}", user);
    response.sendRedirect(INDEX_PAGE);
  }
}
