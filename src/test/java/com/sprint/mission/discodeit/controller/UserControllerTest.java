package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.data.UserStatusDTO;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private BasicUserStatusService userStatusService;

  @MockitoBean
  private JpaMetamodelMappingContext jpaMetamodelMappingContext; // 가상의 jpa을 실행해줄 환경

  @Test
  @DisplayName("유저 생성 binaryContent X")
  void registerUser() throws Exception {
    UserCreateRequest userCreateRequest = new UserCreateRequest("user1", "user1@example.com",
        "password");

    UserDTO userDTO = UserDTO.builder()
        .id(UUID.randomUUID())
        .username(userCreateRequest.username())
        .email(userCreateRequest.email())
        .build();

    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(userCreateRequest) // JSON 직렬화
    );

    given(userService.createUser(userCreateRequest, null)).willReturn(userDTO);

    mockMvc.perform(multipart(HttpMethod.POST, "/api/users")
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value(userCreateRequest.username()))
        .andExpect(jsonPath("$.email").value(userCreateRequest.email()));
  }

  @Test
  @DisplayName("유저 생성 binaryContent O")
  void createUserWithBinaryContent() throws Exception {
    UserCreateRequest userCreateRequest = new UserCreateRequest("user1", "user1@example.com",
        "password");

    // JSON part
    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",   // @RequestPart 이름과 동일해야 함
        "",
        "application/json",
        objectMapper.writeValueAsBytes(userCreateRequest)
    );

    // Binary part (예: 이미지 파일)
    MockMultipartFile binaryPart = new MockMultipartFile(
        "binaryContent",       // 컨트롤러 @RequestPart("binaryContent") 와 일치
        "profile.png",         // 파일 이름
        MediaType.IMAGE_PNG_VALUE,
        "fake-image-content".getBytes() // 테스트용 더미 바이트
    );

    UserDTO userDTO = UserDTO.builder()
        .id(UUID.randomUUID())
        .username(userCreateRequest.username())
        .email(userCreateRequest.email())
        .profile(BinaryContentDTO.builder()
            .id(UUID.randomUUID())
            .fileName(binaryPart.getOriginalFilename())
            .size(binaryPart.getSize())
            .contentType(MediaType.IMAGE_PNG_VALUE)
            .build())
        .build();

    given(userService.createUser(any(UserCreateRequest.class), any())).willReturn(userDTO);

    mockMvc.perform(multipart(HttpMethod.POST, "/api/users")
            .file(userPart)
            .file(binaryPart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value(userCreateRequest.username()))
        .andExpect(jsonPath("$.email").value(userCreateRequest.email()))
        .andExpect(jsonPath("$.profile.fileName").value(binaryPart.getOriginalFilename()));

    verify(userService).createUser(any(UserCreateRequest.class), any());
  }


  @Test
  @DisplayName("유저 수정 (RequestPart)")
  void modifyUserWithRequestPart() throws Exception {
    UUID userId = UUID.randomUUID();
    UserUpdateRequest updateRequest = new UserUpdateRequest("newUser", "new@example.com", null);

    UserDTO updatedUser = UserDTO.builder()
        .id(userId)
        .username(updateRequest.newUsername())
        .email(updateRequest.newEmail())
        .build();

    given(userService.updateUser(eq(updatedUser.id()), any(UserUpdateRequest.class), isNull()))
        .willReturn(updatedUser);

    // JSON requestPart
    MockMultipartFile userPart = new MockMultipartFile(
        "userUpdateRequest", // 컨트롤러 @RequestPart 이름과 맞춰야 함
        "",
        "application/json",
        objectMapper.writeValueAsBytes(updateRequest)
    );

    mockMvc.perform(multipart("/api/users/{id}", userId)
            .file(userPart)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(updateRequest.newUsername()))
        .andExpect(jsonPath("$.email").value(updateRequest.newEmail()));

    verify(userService).updateUser(eq(userId), any(UserUpdateRequest.class), isNull());
  }


  @Test
  @DisplayName("유저 삭제")
  void deleteUser() throws Exception {
    UUID userId = UUID.randomUUID();

    mockMvc.perform(delete("/api/users/{id}", userId))
        .andExpect(status().isNoContent());

    verify(userService).deleteUser(eq(userId));
  }

  @Test
  @DisplayName("유저 단일 조회")
  void findUser() throws Exception {
    UUID userId = UUID.randomUUID();
    UserDTO userDTO = UserDTO.builder()
        .id(userId)
        .username("user1")
        .email("user1@example.com")
        .build();

    given(userService.findByUserId(eq(userId))).willReturn(userDTO);

    mockMvc.perform(get("/api/users/{id}", userId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(userDTO.username()))
        .andExpect(jsonPath("$.email").value(userDTO.email()));

    verify(userService).findByUserId(eq(userId));
  }

  @Test
  @DisplayName("유저 전체 조회")
  void findAllUser() throws Exception {
    UserDTO user1 = UserDTO.builder()
        .id(UUID.randomUUID())
        .username("user1")
        .email("user1@example.com")
        .build();

    UserDTO user2 = UserDTO.builder()
        .id(UUID.randomUUID())
        .username("user2")
        .email("user2@example.com")
        .build();

    given(userService.findAll()).willReturn(List.of(user1, user2));

    mockMvc.perform(get("/api/users")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].username").value(user1.username()))
        .andExpect(jsonPath("$[1].username").value(user2.username()));

  }

  @Test
  @DisplayName("유저 상태 업데이트")
  void updateUserStatus() throws Exception {
    UUID userId = UUID.randomUUID();
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(Instant.now());
    UserStatusDTO statusDTO = UserStatusDTO.builder()
        .userId(userId)
        .lastActiveAt(request.newLastActiveAt())
        .build();

    given(userStatusService.updateByUserId(eq(userId), any(UserStatusUpdateRequest.class)))
        .willReturn(statusDTO);

    mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(userId.toString()));

    verify(userStatusService).updateByUserId(eq(userId), any(UserStatusUpdateRequest.class));
  }
}