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
		User user = userRepository.findById(request.getUserId()).orElseThrow(() ->
			new IllegalArgumentException("user을 찾을 수 없습니다."));

		if (user.getUsername().equals(request.getUsername())) {
			throw new RuntimeException("같은 아이디가 존재합니다.");
		}

		if (user.getEmail().equals(request.getEmail())) {
			throw new RuntimeException("같은 이메일이 존재합니다.");
		}

		User register = new User(request.getUsername(), request.getEmail(), request.getPassword(), request.getBinaryContent().getProfileId());
		userRepository.save(register);

		UserStatus status = new UserStatus(register.getUserId());
		userStatusRepository.save(status);

		// 선택적으로 프로필 이미지를 같이 등록함.
		if (request.getBinaryContent() != null) {
			BinaryContent content = request.getBinaryContent();
			binaryContentRepository.save(content);
		}

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

		user.update(request.getUsername(), request.getEmail(), request.getPassword(), request.getBinaryContent().getProfileId());
		userRepository.save(user);

		// 선택적으로 프로필 이미지 대체 하기.?
		if (request.getBinaryContent() != null) {
			BinaryContent content = request.getBinaryContent();
			binaryContentRepository.save(content);
		}
	}

	@Override
	public void deleteUser(UUID userId) {
		userRepository.delete(userId);
		userStatusRepository.deleteByUserId(userId);
		binaryContentRepository.deleteByUserId(userId);
	}
}
