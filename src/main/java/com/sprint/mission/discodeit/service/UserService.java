package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.User;

public interface UserService {
	public User createUser();
	public Optional<User> findById(UUID uuid);
	public List<User> findByAll();

	public User updateUser(UUID uuid);
	public void deleteUser(UUID uuid);
}
