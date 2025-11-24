package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "유저 관련 API")
@Slf4j
public class UserController {

  private final UserService userService;

  // [ ] 사용자를 등록
  @Operation(summary = "유저 생성 API", responses = {
      @ApiResponse(responseCode = "201", description = "회원이 정상적으로 생성되었습니다."),
      @ApiResponse(responseCode = "400", description = "요청 DTO가 잘못되었습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")})
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDTO> registerUser(
      @RequestPart @Valid UserCreateRequest userCreateRequest,
      @RequestPart(required = false) MultipartFile profile) throws IOException {
    log.info("유저 생성 요청 수신: username={}, email={}", userCreateRequest.username(),
        userCreateRequest.email());
    UserDTO user = userService.createUser(userCreateRequest, profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(user); // 201 Created
  }

  // [ ] 사용자 정보 수정
  @Operation(summary = "유저 수정 API", responses = {
      @ApiResponse(responseCode = "200", description = "회원 정보가 정상적으로 수정되었습니다."),
      @ApiResponse(responseCode = "400", description = "요청 DTO가 잘못되었습니다."),
      @ApiResponse(responseCode = "404", description = "잘못된 사용자 ID가 포함되었습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")})
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDTO> modifyUser(@PathVariable UUID userId,
      @RequestPart @Valid UserUpdateRequest userUpdateRequest,
      @RequestPart(required = false) MultipartFile profile) throws IOException {
    log.info("유저 업데이트 요청 수신");
    UserDTO user = userService.updateUser(userId, userUpdateRequest, profile);
    return ResponseEntity.status(HttpStatus.OK).body(user); // 200 OK
  }

  // [ ] 사용자 삭제
  @Operation(summary = "유저 삭제 API", responses = {
      @ApiResponse(responseCode = "204", description = "회원이 정상적으로 삭제되었습니다."),
      @ApiResponse(responseCode = "404", description = "잘못된 사용자 ID가 포함되었습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")})
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    log.info("유저 삭제 요청 수신");
    userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
  }

  // [ ] 단일 사용자 조회
  @Operation(summary = "유저 단일 조회 API", responses = {
      @ApiResponse(responseCode = "200", description = "회원이 정상적으로 조회되었습니다."),
      @ApiResponse(responseCode = "404", description = "잘못된 사용자 ID가 포함되었습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")})
  @GetMapping("/{userId}")
  public ResponseEntity<UserDTO> findUser(@PathVariable UUID userId) {
    UserDTO user = userService.findByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(user); // 200 OK
  }

  // [ ] 모든 사용자 조회
  @Operation(summary = "유저 전체 조회 API", responses = {
      @ApiResponse(responseCode = "200", description = "회원 목록이 정상적으로 조회되었습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")})
  @GetMapping
  public ResponseEntity<List<UserDTO>> findAllUser() {
    List<UserDTO> user = userService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(user); // 200 OK
  }

}
