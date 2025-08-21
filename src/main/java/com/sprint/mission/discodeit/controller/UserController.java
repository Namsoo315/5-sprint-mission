package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {

    private final UserService userService;
    private final BasicUserStatusService userStatusService;

    // [ ] 사용자를 등록
    @Operation(summary = "유저 생성 API", responses = {
            @ApiResponse(responseCode = "201", description = "회원이 정상적으로 생성되었습니다."),
            @ApiResponse(responseCode = "400", description = "요청 DTO가 잘못되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
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
    @Operation(summary = "유저 수정 API", responses = {
            @ApiResponse(responseCode = "200", description = "회원 정보가 정상적으로 수정되었습니다."),
            @ApiResponse(responseCode = "400", description = "요청 DTO가 잘못되었습니다."),
            @ApiResponse(responseCode = "404", description = "잘못된 사용자 ID가 포함되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> modifyUser(
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
        User user = userService.updateUser(userId, userUpdateRequest, binaryContentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(user); // 200 OK
    }

    // [ ] 사용자 삭제
    @Operation(summary = "유저 삭제 API", responses = {
            @ApiResponse(responseCode = "204", description = "회원이 정상적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "잘못된 사용자 ID가 포함되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
    }

    // [ ] 단일 사용자 조회
    @Operation(summary = "유저 단일 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "회원이 정상적으로 조회되었습니다."),
            @ApiResponse(responseCode = "404", description = "잘못된 사용자 ID가 포함되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUser(@PathVariable UUID userId) {
        UserDto userDto = userService.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userDto); // 200 OK
    }

    // [ ] 모든 사용자 조회
    @Operation(summary = "유저 전체 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "회원 목록이 정상적으로 조회되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUser() {
        List<UserDto> userDto = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(userDto); // 200 OK
    }

    // [ ] 사용자의 온라인 상태 업데이트
    @Operation(summary = "유저 상태 업데이트 API", responses = {
            @ApiResponse(responseCode = "200", description = "회원 상태가 정상적으로 수정되었습니다."),
            @ApiResponse(responseCode = "400", description = "요청 DTO가 잘못되었습니다."),
            @ApiResponse(responseCode = "404", description = "잘못된 사용자 ID가 포함되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatus> updateUserStatus(
            @PathVariable UUID userId,
            @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = userStatusService.updateByUserId(userId, userStatusUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userStatus); // 200 OK
    }
}
