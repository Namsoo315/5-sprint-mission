package com.sprint.mission.discodeit.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
	public ResponseEntity<User> registerUser(@RequestPart UserCreateRequest userCreateRequest,
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

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// [ ] 사용자 정보를 수정할 수 있다.
	@RequestMapping(path = "/modify",
		method = RequestMethod.PATCH,
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> modifyUser(@RequestPart UserUpdateRequest userUpdateRequest,
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

		return new ResponseEntity<>("수정이 완료 되었습니다. : " + userUpdateRequest.userId(), HttpStatus.OK);
	}

	// [ ] 사용자를 삭제할 수 있다.
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteUser(@PathVariable("id") UUID userId) {
		userService.deleteUser(userId);
		return new ResponseEntity<>("유저가 삭제되었습니다. : " + userId, HttpStatus.OK);
	}

	// [ ] 모든 사용자를 조회할 수 있다.
	@RequestMapping(path = "/findAll", method = RequestMethod.GET)
	public ResponseEntity<List<UserDto>> findAllUser() {
		List<UserDto> response = userService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// [ ] 사용자의 온라인 상태를 업데이트할 수 있다.
	@RequestMapping(path = "/update/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<UserStatus> updateUserStatus(@PathVariable("id") UUID userId) {
		UserStatus response = userStatusService.updateByUserId(userId);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
