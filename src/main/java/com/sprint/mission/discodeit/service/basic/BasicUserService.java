package com.sprint.mission.discodeit.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserFindRequest;
import com.sprint.mission.discodeit.dto.UserFindResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;

import lombok.RequiredArgsConstructor;

@Service("userService")
@RequiredArgsConstructor
public class BasicUserService implements UserService {
	private final UserRepository userRepository;
	private final BinaryContentRepository contentRepository;
	private final UserStatusRepository statusRepository;

	@Override
	public User createUser(UserCreateRequest request) {

		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new RuntimeException("같은 이메일이 존재합니다.");
		}

		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new RuntimeException("같은 이름이 존재합니다.");
		}

		User user = new User(request.getUsername(), request.getEmail(), request.getPassword());
		userRepository.save(user);

		UserStatus status = new UserStatus(user.getUserId());
		statusRepository.save(status);

		// 프로필 이미지 저장 (선택) 만들어야 함.
		if (request.getContent() != null) {
			BinaryContent content = new BinaryContent(user.getUserId(), request.getContent().getBinaryContent());
			contentRepository.save(content);
		}

		return user;
	}

	@Override
	public Optional<UserFindResponse> findByUserId(UserFindRequest request) {
		return userRepository.findById(request.getUserId())
			.map(user -> {
				UserStatus status = statusRepository.findById(user.getUserId()).orElse(null);
				return new UserFindResponse(user, status);
			});
	}

	@Override
	public List<UserFindResponse> findAll() {
		List<User> users = userRepository.findAll();
		List<UserFindResponse> responses = new ArrayList<>();

		for (User user : users) {
			UserStatus status = statusRepository.findById(user.getUserId()).orElse(null);
			responses.add(new UserFindResponse(user, status));
		}

		return responses;
	}

	@Override
	public void updateUser(UserUpdateRequest request) {

		User user = userRepository.findById(request.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

		user.update(request.getUsername(), request.getEmail(), request.getPassword());
		userRepository.save(user);

		if (request.getContent() != null) {
			contentRepository.deleteByOwnerId(user.getUserId());
			BinaryContent content = new BinaryContent(user.getUserId(), request.getContent().getBinaryContent());
			contentRepository.save(content);
		}
	}

	@Override
	public void deleteUser(UUID uuid) {
		userRepository.delete(uuid);
		statusRepository.deleteByUserId(uuid);
		contentRepository.deleteByOwnerId(uuid);
	}
}
