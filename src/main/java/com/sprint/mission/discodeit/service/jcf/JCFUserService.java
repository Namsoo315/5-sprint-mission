package com.sprint.mission.discodeit.service.jcf;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

public class JCFUserService implements UserService {
	private final UserRepository repo;

	public JCFUserService(UserRepository repo) {
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
	public void updateUser(UUID uuid, String username, int age) {

		User user = repo.findById(uuid).orElse(null);

		if (user == null) {
			throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
		}

		user.update(username, age);

		repo.save(user);
	}

	@Override
	public void deleteUser(UUID uuid) {
		repo.delete(uuid);
	}
}
