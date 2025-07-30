package com.sprint.mission.discodeit.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import lombok.RequiredArgsConstructor;

@Service("userService")
@RequiredArgsConstructor
public class BasicUserService implements UserService {
	private final UserRepository repo;

	@Override
	public User createUser(String name, int age) {
		User user = new User(name, age);
		repo.save(user);

		return user;
	}

	@Override
	public Optional<User> findByUserId(UUID uuid) {
		return repo.findById(uuid);
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(repo.findAll());
	}

	@Override
	public void updateUser(UUID uuid, String name, int age) {

		User user = repo.findById(uuid).orElse(null);

		if (user == null) {
			throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
		}

		user.update(name, age);

		repo.save(user);
	}

	@Override
	public void deleteUser(UUID uuid) {
		repo.delete(uuid);
	}
}
