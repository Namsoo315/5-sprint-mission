package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestPart;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {

  private final MessageService messageService;
  private final SimpMessagingTemplate simpMessagingTemplate;

  @MessageMapping("/messages")
  public void sendMessage(
      @RequestPart MessageCreateRequest request,
      @AuthenticationPrincipal DiscodeitUserDetails userDetails) throws IOException {
    // 일단 임시
    MessageDTO message = messageService.createMessage(request, null);

    String destination = "/sub/channels." + request.channelId() + ".messages";
    simpMessagingTemplate.convertAndSend(destination, message);

    log.info("메시지 전송 완료 {}", destination);
  }

}
