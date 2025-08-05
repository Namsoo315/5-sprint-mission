package com.sprint.mission.discodeit.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

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
	public User createUser(UserCreateRequest request) {

		UUID profileId = null;
		// 1. 선택적으로 프로필 이미지를 같이 등록함. 있으면 등록 없으면 등록 안함.
		if (request.getBinaryContent() != null) {
			BinaryContent content = new BinaryContent(request.getFileName(), request.getContentType(),
				request.getSize(), request.getBinaryContent());
			binaryContentRepository.save(content);
			profileId = content.getBinaryContentId();
		}

		// 2. username, email 호환성 확인
		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new RuntimeException("같은 아이디가 존재합니다.");
		}

		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new RuntimeException("같은 이메일이 존재합니다.");
		}
		User user = new User(request.getUsername(), request.getEmail(), request.getPassword(), profileId);
		userRepository.save(user);

		// 3. userStatus 같이 생성.
		UserStatus status = new UserStatus(user.getUserId());
		userStatusRepository.save(status);

		return user;
	}

	@Override
	public Optional<UserFindResponse> findByUserId(UserFindRequest request) {
		User user = userRepository.findById(request.getUserId()).orElseThrow(()
			-> new IllegalArgumentException("존재하지 않는 회원입니다."));
		UserStatus userStatus = userStatusRepository.findByUserId(request.getUserId()).orElseThrow(()
			-> new IllegalArgumentException("존재하지 않는 회원의 상태 입니다."));

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

		// 사용자의 온라인 상태 정보를 같이 포함하는 로직.
		for (User user : users) {
			UserStatus status = userStatusRepository.findByUserId(user.getUserId()).orElseThrow(()
				-> new IllegalArgumentException("존재하지 않는 회원의 상태 입니다."));

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
	public void updateUser(UserUpdateRequest request) {

		User user = userRepository.findById(request.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

		user.update(request.getUsername(), request.getEmail(), request.getPassword(),
			request.getBinaryContent().getBinaryContentId());
		userRepository.save(user);

		if (request.getBinaryContent() != null) {
			BinaryContent content = request.getBinaryContent();
			binaryContentRepository.save(content);
		}
	}

	@Override
	public void deleteUser(UUID userId) {
		userRepository.delete(userId);
		userStatusRepository.deleteByUserId(userId);
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
		binaryContentRepository.delete(user.getProfileId());
	}
}
