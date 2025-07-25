package com.sprint.mission.discodeit.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

public class BasicUserService implements UserService {
	private final UserRepository repo;

	public BasicUserService(UserRepository repo) {
		this.repo = repo;
	}

	@Override
	public User createUser(String name, int age) {
		User user = new User(name, age);
		repo.save(user);

		return user;
	}

	@Override
	public Optional<User> findById(UUID uuid) {
		return repo.findById(uuid);
	}

	@Override
	public List<User> findByAll() {
		return new ArrayList<>(repo.findAll());
	}

	@Override
	public void updateUser(UUID uuid, String name, int age) {

		User existsUser = repo.findById(uuid).orElse(null);

		if (existsUser == null) {
			throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
		}

		existsUser.setUsername(name);
		existsUser.setAge(age);

		repo.save(existsUser);
	}

	@Override
	public void deleteUser(UUID uuid) {
		repo.delete(uuid);
	}
}
