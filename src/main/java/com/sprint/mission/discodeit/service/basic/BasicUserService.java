package com.sprint.mission.discodeit.service.basic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserFindRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
	private final UserRepository userRepository;
	private final BinaryContentRepository binaryContentRepository;
	private final UserStatusRepository userStatusRepository;

	@Override
	public User createUser(UserCreateRequest userCreateRequest, BinaryContentDTO binaryContentDTO) {

		// 1. username, email 호환성 확인
		if (userRepository.findByUsername(userCreateRequest.username()).isPresent()) {
			throw new RuntimeException("같은 아이디가 존재합니다.");
		}

		if (userRepository.findByEmail(userCreateRequest.email()).isPresent()) {
			throw new RuntimeException("같은 이메일이 존재합니다.");
		}

		UUID profileId = null;

		// 2. 선택적으로 프로필 이미지를 같이 등록함. 있으면 등록 없으면 등록 안함.
		if (binaryContentDTO != null &&
			binaryContentDTO.binaryContent() != null &&
			binaryContentDTO.binaryContent().length > 0) {

			BinaryContent content = new BinaryContent(binaryContentDTO.fileName(), binaryContentDTO.contentType(),
				binaryContentDTO.size(), binaryContentDTO.binaryContent());
			binaryContentRepository.save(content);
			profileId = content.getBinaryContentId();
		}

		// 3. user, userStatus 같이 생성.
		User user = new User(userCreateRequest.username(), userCreateRequest.email(),
			userCreateRequest.password(), profileId);
		userRepository.save(user);

		UserStatus status = new UserStatus(user.getUserId(), Instant.MIN);
		userStatusRepository.save(status);

		return user;
	}

	@Override
	public UserDto findByUserId(UserFindRequest request) {

		// 1. 호환성 체크	user, userStatus Id(toDto가 함) 체크
		User user = userRepository.findById(request.userId()).orElseThrow(()
			-> new NoSuchElementException("존재하지 않는 회원입니다."));

		return toDto(user);
	}

	@Override
	public List<UserDto> findAll() {
		List<User> users = userRepository.findAll();
		List<UserDto> responses = new ArrayList<>();

		for (User user : users) {
			// 호환성 체크도 toDto가 하게 됨.
			responses.add(toDto(user));
		}

		return responses;
	}

	@Override
	public void updateUser(UserUpdateRequest userUpdateRequest, BinaryContentDTO binaryContentDTO) {

		UUID profileId = null;
		// 1. 선택적으로 프로필 이미지를 같이 등록함. (있으면 등록 없으면 등록 안함.)
		if (binaryContentDTO != null &&
			binaryContentDTO.binaryContent() != null &&
			binaryContentDTO.binaryContent().length > 0) {
			BinaryContent content = new BinaryContent(binaryContentDTO.fileName(),
				binaryContentDTO.contentType(),
				binaryContentDTO.size(), binaryContentDTO.binaryContent());
			binaryContentRepository.save(content);
			profileId = content.getBinaryContentId();
		}

		// 2. User 호환성 체크
		User user = userRepository.findById(userUpdateRequest.userId())
			.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

		// 3. Update 작업 수행.
		user.update(userUpdateRequest.username(), userUpdateRequest.email(), userUpdateRequest.password(),
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

	// Dto를 사용하는 메서드 분리
	private UserDto toDto(User user) {
		Boolean online = userStatusRepository.findByUserId(user.getUserId())
			.map(UserStatus::isOnline)
			.orElse(null);

		return new UserDto(
			user.getUserId(),
			user.getCreatedAt(),
			user.getUpdatedAt(),
			user.getUsername(),
			user.getEmail(),
			user.getProfileId(),
			online
		);
	}
}
