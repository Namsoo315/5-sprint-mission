package com.sprint.mission.discodeit.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserFindRequest;
import com.sprint.mission.discodeit.dto.user.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;

import lombok.RequiredArgsConstructor;

@Service("userService")
@RequiredArgsConstructor
public class BasicUserService implements UserService {
	private final UserRepository userRepository;
	private final BinaryContentRepository binaryContentRepository;
	private final UserStatusRepository userStatusRepository;

	@Override
	public User createUser(UserCreateRequest userCreateRequest, BinaryContentDTO binaryContentDTO) {

		UUID profileId = null;

		// 1. 선택적으로 프로필 이미지를 같이 등록함. 있으면 등록 없으면 등록 안함.
		if (binaryContentDTO != null &&
			binaryContentDTO.getBinaryContent() != null &&
			binaryContentDTO.getBinaryContent().length > 0) {

			BinaryContent content = new BinaryContent(binaryContentDTO.getFileName(), binaryContentDTO.getContentType(),
				binaryContentDTO.getSize(), binaryContentDTO.getBinaryContent());
			binaryContentRepository.save(content);
			profileId = content.getBinaryContentId();
		}

		// 2. username, email 호환성 확인
		if (userRepository.findByUsername(userCreateRequest.getUsername()).isPresent()) {
			throw new RuntimeException("같은 아이디가 존재합니다.");
		}

		if (userRepository.findByEmail(userCreateRequest.getEmail()).isPresent()) {
			throw new RuntimeException("같은 이메일이 존재합니다.");
		}

		// 3. user, userStatus 같이 생성.
		User user = new User(userCreateRequest.getUsername(), userCreateRequest.getEmail(),
			userCreateRequest.getPassword(), profileId);
		userRepository.save(user);

		UserStatus status = new UserStatus(user.getUserId());
		userStatusRepository.save(status);

		return user;
	}

	@Override
	public Optional<UserFindResponse> findByUserId(UserFindRequest request) {

		// 1. 호환성 체크	user, userStatus Id 체크
		User user = userRepository.findById(request.getUserId()).orElseThrow(()
			-> new IllegalArgumentException("존재하지 않는 회원입니다."));
		UserStatus userStatus = userStatusRepository.findByUserId(request.getUserId()).orElseThrow(()
			-> new IllegalArgumentException("존재하지 않는 회원의 상태 입니다."));

		// 2. 사용자의 온라인 상태 정보를 포함함 (단 password는 포함 X)
		return Optional.of(UserFindResponse.builder()
			.userId(user.getUserId())
			.username(user.getUsername())
			.email(user.getEmail())
			.status(userStatus.isStatus())
			.build());
	}

	@Override
	public List<UserFindResponse> findAll() {
		List<User> users = userRepository.findAll();
		List<UserFindResponse> responses = new ArrayList<>();

		for (User user : users) {

			// 1. 호환성 체크	user, userStatus Id 체크
			userRepository.findById(user.getUserId()).orElseThrow(
				() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

			UserStatus status = userStatusRepository.findByUserId(user.getUserId()).orElseThrow(
				() -> new IllegalArgumentException("존재하지 않는 회원의 상태 입니다."));

			// 2. 사용자의 온라인 상태 정보를 포함함 (단 password는 포함 X)
			responses.add(UserFindResponse.builder()
				.userId(user.getUserId())
				.username(user.getUsername())
				.email(user.getEmail())
				.status(status.isStatus())
				.build());
		}

		return responses;
	}

	@Override
	public void updateUser(UserUpdateRequest userUpdateRequest, BinaryContentDTO binaryContentDTO) {

		UUID profileId = null;
		// 1. 선택적으로 프로필 이미지를 같이 등록함. 있으면 등록 없으면 등록 안함.
		if (binaryContentDTO != null &&
			binaryContentDTO.getBinaryContent() != null &&
			binaryContentDTO.getBinaryContent().length > 0) {
			BinaryContent content = new BinaryContent(binaryContentDTO.getFileName(),
				binaryContentDTO.getContentType(),
				binaryContentDTO.getSize(), binaryContentDTO.getBinaryContent());
			binaryContentRepository.save(content);
			profileId = content.getBinaryContentId();
		}

		// 2. User 호환성 체크
		User user = userRepository.findById(userUpdateRequest.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

		// 3. Update 작업 수행.
		user.update(userUpdateRequest.getUsername(), userUpdateRequest.getEmail(), userUpdateRequest.getPassword(),
			profileId);

		userRepository.save(user);

	}

	@Override
	public void deleteUser(UUID userId) {

		// 1. 관련 도메인도 같이 삭제 User, UserStatus, BinaryContent

		// 2. user 안에 있는 profileId -> BinaryContentId 삭제
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

		if (user.getProfileId() != null) {
			binaryContentRepository.delete(user.getProfileId());
		}

		userStatusRepository.deleteByUserId(userId);
		userRepository.delete(userId);
	}
}
