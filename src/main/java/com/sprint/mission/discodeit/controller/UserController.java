package com.sprint.mission.discodeit.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sprint.mission.discodeit.dto.ApiResponse;
import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {

  private final UserService userService;
  private final BasicUserStatusService userStatusService;

  //[ ] 사용자를 등록할 수 있다.
  @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<User>> registerUser(
      @RequestPart UserCreateRequest userCreateRequest,
      @RequestPart(required = false) MultipartFile profile) throws IOException {

    BinaryContentDTO binaryContentDTO = null;

    if (profile != null) {
      binaryContentDTO = new BinaryContentDTO(
          profile.getOriginalFilename(),
          profile.getContentType(),
          profile.getSize(),
          profile.getBytes());
    }

    User user = userService.createUser(userCreateRequest, binaryContentDTO);

    return ResponseEntity.ok(ApiResponse.ok(user, "유저 생성 완료"));
  }

  // [ ] 사용자 정보를 수정할 수 있다.
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<String>> modifyUser(
      @PathVariable UUID userId,
      @RequestPart UserUpdateRequest userUpdateRequest,
      @RequestPart(required = false) MultipartFile profile) throws IOException {

    BinaryContentDTO binaryContentDTO = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContentDTO = new BinaryContentDTO(
          profile.getOriginalFilename(),
          profile.getContentType(),
          profile.getSize(),
          profile.getBytes());
    }

    userService.updateUser(userId, userUpdateRequest, binaryContentDTO);

    return ResponseEntity.ok(ApiResponse.ok(userId + "님의 정보 수정 완료"));
  }

  // [ ] 사용자를 삭제할 수 있다.
  @DeleteMapping("/{userId}")
  public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok(ApiResponse.ok(userId + "님 삭제 완료"));
  }

  // 단일 사용자 조회
  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse<UserDto>> findUser(@PathVariable UUID userId) {
    UserDto userDto = userService.findByUserId(userId);
    return ResponseEntity.ok(ApiResponse.ok(userDto, "사용자 조회 완료"));
  }

  // [ ] 모든 사용자를 조회할 수 있다.
  @GetMapping
  public ResponseEntity<ApiResponse<List<UserDto>>> findAllUser() {
    List<UserDto> userDto = userService.findAll();
    return ResponseEntity.ok(ApiResponse.ok(userDto, "모든 사용자 조회 완료"));
  }

  // [ ] 사용자의 온라인 상태를 업데이트할 수 있다.
  @PatchMapping("/{userId}/status")
  public ResponseEntity<ApiResponse<String>> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {
    userStatusService.updateByUserId(userId, userStatusUpdateRequest);

    return ResponseEntity.ok(
        ApiResponse.ok(userId + "님의 온라인 상태 정보 업데이트 완료"));
  }
}
