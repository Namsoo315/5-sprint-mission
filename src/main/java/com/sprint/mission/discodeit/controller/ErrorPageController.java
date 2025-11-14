package com.sprint.mission.discodeit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController {

  @GetMapping("/error/expired")
  public String expiredPage() {
    return "error/expired";
  }

}
