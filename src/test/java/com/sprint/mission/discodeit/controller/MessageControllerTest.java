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
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
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

  private UUID channelId;

  @BeforeEach
  void setup() {
    channelId = UUID.fromString("2a027fc5-a0bd-4b36-9baa-7a4c6610e4f0");

    Channel channel = channelRepository.findById(channelId).orElseThrow();
    User author = userRepository.findAll().stream().findFirst().orElseThrow();

    // 테스트 전 메시지 초기화 후 대량 삽입
    messageRepository.deleteByChannelId(channelId);

    IntStream.range(0, 10_000).forEach(i -> {
      Message m = Message.builder()
          .channel(channel)
          .author(author)
          .content("bulk message " + i)
          .createdAt(Instant.now().plusMillis(i)) // 순차 증가
          .updatedAt(Instant.now().plusMillis(i))
          .build();
      messageRepository.save(m);
    });
  }

  @Test
  @Transactional
  void cursorPagingPerformance() throws Exception {
    Instant cursorInstant = Instant.now().minusSeconds(3);

    long start = System.currentTimeMillis();
    mockMvc.perform(get("/api/messages/cursor")   // ✅ 커서 전용 엔드포인트
            .param("channelId", channelId.toString())
            .param("cursor", cursorInstant.toString())
            .param("size", "50"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(lessThanOrEqualTo(50)))
        .andExpect(jsonPath("$.content[0].createdAt").value(greaterThan(cursorInstant.toString())));
    long end = System.currentTimeMillis();

    System.out.println("커서 페이징 실행 시간: " + (end - start) + "ms");
  }

  @Test
  @Transactional
  void offsetPagingPerformance() throws Exception {
    long start = System.currentTimeMillis();
    mockMvc.perform(get("/api/messages/page")   // ✅ 페이지 전용 엔드포인트
            .param("channelId", channelId.toString())
            .param("size", "50")
            .param("page", "100"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(lessThanOrEqualTo(50)));
    long end = System.currentTimeMillis();

    System.out.println("기본 OFFSET 페이징 실행 시간: " + (end - start) + "ms");
  }
}
