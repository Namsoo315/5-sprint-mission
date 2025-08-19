package com.sprint.mission.discodeit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {

  // 루트 경로를 정적 HTML로 리다이렉트
  @GetMapping("/users")
  public String index() {
    return "redirect:/general/user-list.html";
  }

  // [ ] 생성형 AI (Claude, ChatGPT 등)를 활용해서 위 이미지와 비슷한 화면을 생성 후 서빙해보세요.
  @GetMapping("/users2")
  public String index2() {
    return "redirect:/required/user-list.html";
  }
}
