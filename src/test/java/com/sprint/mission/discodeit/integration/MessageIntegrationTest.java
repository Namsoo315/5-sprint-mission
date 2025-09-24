package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MessageIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MessageRepository messageRepository;

  private Channel channel;
  private User author;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setup() {

    channel = Channel.builder()
        .name("newChannel")
        .description("새 채널")
        .type(ChannelType.PUBLIC)
        .build();
    channel = channelRepository.save(channel);

    author = User.builder()
        .username("testUser2")
        .email("testUser2@example.com")
        .password("password")
        .build();
    author = userRepository.save(author);

    // 테스트 전 메시지 초기화 후 대량 삽입
    messageRepository.deleteByChannelId(channel.getId());

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
  @DisplayName("10000개의 더미데이터로 페이징 ")
  void cursorPagingPerformance() throws Exception {
    Instant cursorInstant = Instant.now().minusSeconds(3);

    long start = System.currentTimeMillis();
    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.getId().toString())
            .param("cursor", cursorInstant.toString())
            .param("size", "50"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(lessThanOrEqualTo(50)))
        .andExpect(jsonPath("$.content[0].createdAt").value(greaterThan(cursorInstant.toString())));
    long end = System.currentTimeMillis();

    System.out.println("커서 페이징 실행 시간: " + (end - start) + "ms");
  }


}
