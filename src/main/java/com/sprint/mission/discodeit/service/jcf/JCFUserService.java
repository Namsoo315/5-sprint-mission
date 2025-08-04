package com.sprint.mission.discodeit.service.jcf;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

public class JCFUserService implements UserService {
	private final UserRepository userRepository;

	public JCFUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User createUser(String name, int age) {
		User user = new User(name, age);
		userRepository.save(user);

		return user;
	}

	@Override
	public Optional<User> findByUserId(UUID uuid) {
		return userRepository.findById(uuid);
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(userRepository.findAll());
	}

	@Override
	public void updateUser(UUID uuid, String username, int age) {

		User user = userRepository.findById(uuid).orElse(null);

		if (user == null) {
			throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
		}

		user.update(username, age);

		userRepository.save(user);
	}

	@Override
	public void deleteUser(UUID uuid) {
		userRepository.delete(uuid);
	}
}
