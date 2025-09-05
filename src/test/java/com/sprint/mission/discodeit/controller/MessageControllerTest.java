package com.sprint.mission.discodeit.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  @Transactional
  void findMessageByChannelId() throws Exception {
    UUID channelId = UUID.fromString("c84e32df-b7aa-4452-820c-b5edbadbb824");

    // 테스트용 채널, 유저 조회
    Channel channel = channelRepository.findById(channelId).orElseThrow();
    User author = userRepository.findAll().stream().findFirst().orElseThrow();

    // 1. 기본 조회
    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("size", "5"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(lessThanOrEqualTo(5)))
        .andExpect(jsonPath("$.hasNext").isBoolean());

    // 2. cursor 지정 후 조회
    LocalDateTime cursorLdt = LocalDateTime.now().minusDays(1);
    Instant cursorInstant = cursorLdt.atZone(ZoneId.systemDefault()).toInstant();

    // 테스트용 메시지 생성 (cursor 이후에 생성되도록)
    Message testMessage = Message.builder()
        .channel(channel)
        .author(author)
        .content("테스트 메시지")
        .createdAt(Instant.now()) // 현재 시각
        .updatedAt(Instant.now())
        .build();
    messageRepository.save(testMessage);

    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("cursor", cursorInstant.toString())
            .param("size", "5"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(lessThanOrEqualTo(5)))
        .andExpect(jsonPath("$.content[0].createdAt").value(greaterThan(cursorInstant.toString())));
  }
}
