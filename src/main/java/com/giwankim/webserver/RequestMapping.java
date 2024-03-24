package com.giwankim.webserver;

import com.giwankim.controller.Controller;
import com.giwankim.controller.CreateUserController;
import com.giwankim.controller.ListUserController;
import com.giwankim.controller.LoginController;

import java.util.Map;

public class RequestMapping {
  private RequestMapping() {
  }

  private static final Map<String, Controller> controllers = Map.ofEntries(
    Map.entry("/user/create", new CreateUserController()),
    Map.entry("/user/login", new LoginController()),
    Map.entry("/user/list", new ListUserController())
  );

  public static Controller getController(String path) {
    return controllers.get(path);
  }
}
