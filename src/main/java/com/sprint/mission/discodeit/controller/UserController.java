package com.sprint.mission.discodeit.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {

  private final UserService userService;
  private final BasicUserStatusService userStatusService;

  // [ ] 사용자를 등록
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<User> registerUser(
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
    return ResponseEntity.status(HttpStatus.CREATED).body(user); // 201 Created
  }

  // [ ] 사용자 정보 수정
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> modifyUser(
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
    return ResponseEntity.status(HttpStatus.OK).body(userId + "님의 정보 수정 완료"); // 200 OK
  }

  // [ ] 사용자 삭제
  @DeleteMapping("/{userId}")
  public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userId + "님 삭제 완료"); // 204 No Content
  }

  // [ ] 단일 사용자 조회
  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> findUser(@PathVariable UUID userId) {
    UserDto userDto = userService.findByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(userDto); // 200 OK
  }

  // [ ] 모든 사용자 조회
  @GetMapping
  public ResponseEntity<List<UserDto>> findAllUser() {
    List<UserDto> userDto = userService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(userDto); // 200 OK
  }

  // [ ] 사용자의 온라인 상태 업데이트
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<String> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {
    userStatusService.updateByUserId(userId, userStatusUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK)
        .body(userId + "님의 온라인 상태 정보 업데이트 완료"); // 200 OK
  }
}
