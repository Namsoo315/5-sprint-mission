package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.User;

public interface UserRepository {
	User createUser(String name, int age);
	Optional<User> findById(UUID uuid);
	List<User> findByAll();
	void updateUser(UUID uuid, String username, int age);
	void deleteUser(UUID uuid);
}
