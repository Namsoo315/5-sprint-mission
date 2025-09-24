package com.sprint.mission.discodeit.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserStatusRepository userStatusRepository;

  @Autowired
  private BinaryContentRepository binaryContentRepository;

  private User user1;
  private User user2;

  private BinaryContent binaryContent;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();

    binaryContent = BinaryContent.builder()
        .fileName("dummy.png")
        .contentType("image/png")
        .size(1024L)
        .build();
    binaryContentRepository.save(binaryContent);

    // 테스트 유저 (아바타 없음)
    user1 = userRepository.save(User.builder()
        .username("user1")
        .email("user1@example.com")
        .password("password")
        .build());

    //테스트 유저 (아바타 없음)
    user2 = userRepository.save(User.builder()
        .username("user2")
        .email("user2@example.com")
        .password("password")
        .profile(binaryContent)
        .build());

    userStatusRepository.save(UserStatus.builder()
        .user(user1)
        .lastActiveAt(Instant.now())
        .build());

    userStatusRepository.save(UserStatus.builder()
        .user(user2)
        .lastActiveAt(Instant.now())
        .build());
  }

  @Test
  @DisplayName("유저 생성 테스트 (아바타 포함)")
  void registerUserTest() throws Exception {
    UserCreateRequest request = new UserCreateRequest("newUser", "new@example.com", "password");
    MockMultipartFile userPart = new MockMultipartFile("userCreateRequest", "",
        "application/json",
        objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));
    MockMultipartFile profile = new MockMultipartFile("profile", "profile.png", "image/png",
        "dummy".getBytes());

    mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .file(profile))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("newUser"))
        .andExpect(jsonPath("$.email").value("new@example.com"));
  }

  @Test
  @DisplayName("유저 수정 테스트")
  void modifyUserTest() throws Exception {
    UserUpdateRequest request = new UserUpdateRequest("updatedUser", "updated@example.com",
        "newPassword");
    MockMultipartFile userPart = new MockMultipartFile("userUpdateRequest", "",
        "application/json",
        objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));
    MockMultipartFile profile = new MockMultipartFile("profile", "profile.png", "image/png",
        "dummy".getBytes());

    mockMvc.perform(multipart("/api/users/" + user1.getId())
            .file(userPart)
            .file(profile)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            }))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("updatedUser"))
        .andExpect(jsonPath("$.email").value("updated@example.com"));
  }

  @Test
  @DisplayName("유저 단일 조회 테스트")
  void findUserTest() throws Exception {
    mockMvc.perform(get("/api/users/" + user1.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(user1.getId().toString()))
        .andExpect(jsonPath("$.username").value("user1"));
  }

  @Test
  @DisplayName("유저 전체 조회 테스트")
  void findAllUserTest() throws Exception {
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", not(empty())));
  }

  @Test
  @DisplayName("유저 삭제 테스트")
  void deleteUserTest() throws Exception {
    mockMvc.perform(delete("/api/users/" + user1.getId()))
        .andExpect(status().isNoContent());

    // 삭제 후 조회 시 404 예상
    mockMvc.perform(get("/api/users/" + user1.getId()))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("유저 상태 업데이트 테스트")
  void updateUserStatusTest() throws Exception {
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(Instant.now());

    mockMvc.perform(patch("/api/users/" + user1.getId() + "/userStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(user1.getId().toString()));
  }


}
