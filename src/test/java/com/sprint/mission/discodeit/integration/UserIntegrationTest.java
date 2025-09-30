package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class UserIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BinaryContentRepository binaryContentRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private User savedUser;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();

    BinaryContent dummyProfile = BinaryContent.builder()
        .fileName("dummy.png")
        .contentType("image/png")
        .size(1024L)
        .build();
    binaryContentRepository.save(dummyProfile);

    User dummyUser = User.builder()
        .username("testUser")
        .email("test@example.com")
        .password("password")
        .profile(dummyProfile)
        .build();

    savedUser = userRepository.save(dummyUser);
  }

  @Test
  @DisplayName("전체 조회 (Profile 포함)")
  void getUserList_withProfile_success() throws Exception {
    mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].username").value("testUser"))
        .andExpect(jsonPath("$[0].profile.fileName").value("dummy.png"))
        .andExpect(jsonPath("$[0].profile.contentType").value("image/png"))
        .andExpect(jsonPath("$[0].profile.size").value(1024));
  }

  @Test
  @DisplayName("단일 조회 후 (Profile 포함)")
  void getUserById_success() throws Exception {
    mockMvc.perform(get("/api/users/{id}", savedUser.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("testUser"))
        .andExpect(jsonPath("$.profile.fileName").value("dummy.png"));
  }

  @Test
  @DisplayName("업데이트 유저 성공 케이스")
  void updateUser_success() throws Exception {
    MockMultipartFile userPart = new MockMultipartFile(
        "userUpdateRequest", "", "application/json", objectMapper.writeValueAsBytes(
        new UserUpdateRequest("updatedUser", "updated@example.com", null)
    )
    );

    mockMvc.perform(multipart("/api/users/{id}", savedUser.getId())
            .file(userPart)
            .with(request -> {
              request.setMethod("PATCH"); // multipart는 기본 POST, PATCH로 강제 변경
              return request;
            })
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("updatedUser"))
        .andExpect(jsonPath("$.email").value("updated@example.com"))
        .andExpect(jsonPath("$.profile.fileName").value("dummy.png"));
  }
}
