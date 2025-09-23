package com.sprint.mission.discodeit.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  private User user1;
  private User user2;

  @BeforeEach
  void setup() {
    channelRepository.deleteAll();
    userRepository.deleteAll();

    user1 = userRepository.save(User.builder()
        .username("user1")
        .email("user1@example.com")
        .password("password")
        .build());

    user2 = userRepository.save(User.builder()
        .username("user2")
        .email("user2@example.com")
        .password("password")
        .build());
  }

  @Test
  @DisplayName("공개 채널 생성 테스트")
  void createPublicChannelTest() throws Exception {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("publicChannel", "공개 채널");

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("publicChannel"))
        .andExpect(jsonPath("$.description").value("공개 채널"));
  }

  @Test
  @DisplayName("비공개 채널 생성 테스트")
  void createPrivateChannelTest() throws Exception {
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
        List.of(user1.getId(), user2.getId()));

    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());
  }


  @Test
  @DisplayName("비공개 채널 생성 실패 테스트 (2명 이상 생성 가능)")
  void createPrivateChannelFailTest() throws Exception {
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
        List.of(user1.getId()));

    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }


  @Test
  @DisplayName("공개 채널 수정 테스트")
  void updatePublicChannelTest() throws Exception {
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest("channelToUpdate",
        "desc");
    String channelId = objectMapper.readTree(mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andReturn().getResponse().getContentAsString()).get("id").asText();

    ChannelUpdateRequest updateRequest = new ChannelUpdateRequest("updatedName", "updatedDesc");

    mockMvc.perform(patch("/api/channels/" + channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("updatedName"))
        .andExpect(jsonPath("$.description").value("updatedDesc"));
  }


  @Test
  @DisplayName("채널 삭제 테스트")
  void deleteChannelTest() throws Exception {
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest("channelToDelete",
        "desc");
    String channelId = objectMapper.readTree(mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andReturn().getResponse().getContentAsString()).get("id").asText();

    mockMvc.perform(delete("/api/channels/" + channelId))
        .andExpect(status().isNoContent());
  }


  @Test
  @DisplayName("특정 사용자의 채널 조회 테스트")
  void findChannelsByUserIdTest() throws Exception {
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest("channel1", "desc");
    mockMvc.perform(post("/api/channels/public")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createRequest)));

    mockMvc.perform(get("/api/channels")
            .param("userId", user1.getId().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", not(empty())));
  }
}
