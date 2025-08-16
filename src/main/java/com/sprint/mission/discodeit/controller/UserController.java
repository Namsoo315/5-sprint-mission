package com.sprint.mission.discodeit.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.exception.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
	private final UserService userService;
	private final BasicUserStatusService userStatusService;

	//[ ] 사용자를 등록할 수 있다.
	@RequestMapping(path = "/",
		method = RequestMethod.POST,
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<User>> registerUser(@RequestPart UserCreateRequest userCreateRequest,
													@RequestPart(required = false) MultipartFile profile) throws IOException {

		BinaryContentDTO binaryContentDTO = null;

		if (profile != null) {
			binaryContentDTO = new BinaryContentDTO(
				profile.getOriginalFilename(),
				profile.getContentType(),
				profile.getSize(),
				profile.getBytes());
		}

		User response = userService.createUser(userCreateRequest, binaryContentDTO);

		return ResponseEntity.ok(ApiResponse.ok(response, "유저 생성 완료"));
	}

	// [ ] 사용자 정보를 수정할 수 있다.
	@RequestMapping(path = "/modify",
		method = RequestMethod.PATCH,
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<String>> modifyUser(@RequestPart UserUpdateRequest userUpdateRequest,
		@RequestPart(required = false) MultipartFile profile) throws IOException {

		BinaryContentDTO binaryContentDTO = null;
		if (profile != null) {
			binaryContentDTO = new BinaryContentDTO(
				profile.getOriginalFilename(),
				profile.getContentType(),
				profile.getSize(),
				profile.getBytes());
		}

		userService.updateUser(userUpdateRequest, binaryContentDTO);

		return ResponseEntity.ok(ApiResponse.ok(userUpdateRequest.userId() + "님의 정보 수정 완료"));
	}

	// [ ] 사용자를 삭제할 수 있다.
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable("id") UUID userId) {
		userService.deleteUser(userId);
		return ResponseEntity.ok(ApiResponse.ok(userId + "님 삭제 완료"));
	}

	// [ ] 모든 사용자를 조회할 수 있다.
	@RequestMapping(path = "/findAll", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse<List<UserDto>>> findAllUser() {
		List<UserDto> response = userService.findAll();
		return ResponseEntity.ok(ApiResponse.ok(response, "모든 사용자 조회 완료"));
	}

	// [ ] 사용자의 온라인 상태를 업데이트할 수 있다.
	@RequestMapping(path = "/update/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse<String>> updateUserStatus(@PathVariable("id") UUID userId) {
		UserStatus response = userStatusService.updateByUserId(userId);

		return ResponseEntity.ok(ApiResponse.ok(userId + "님의 온라인 상태 정보 업데이트 완료"));
	}
}
