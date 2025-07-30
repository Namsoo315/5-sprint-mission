package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.User;

public interface UserService {
	User createUser(String name, int age);

	Optional<User> findByUserId(UUID uuid);

	List<User> findAll();

	void updateUser(UUID uuid, String username, int age);

	void deleteUser(UUID uuid);
}
