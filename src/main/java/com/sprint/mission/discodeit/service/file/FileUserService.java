package com.sprint.mission.discodeit.service.file;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

public class FileUserService implements UserService {
	UserRepository repo;

	public FileUserService(UserRepository repo) {
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
		return repo.findAll();
	}

	@Override
	public void updateUser(UUID uuid, String username, int age) {
		User user = repo.findById(uuid).orElse(null);

		if (user == null) {
			throw new IllegalArgumentException("아이디가 존재하지 않습니다.");
		}

		user.setAge(age);
		user.setUsername(username);
		repo.save(user);
	}

	@Override
	public void deleteUser(UUID uuid) {
		repo.delete(uuid);
	}
}
