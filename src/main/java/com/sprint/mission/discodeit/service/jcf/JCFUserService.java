package com.sprint.mission.discodeit.service.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

public class JCFUserService implements UserService {
	private final Map<UUID, User> map = new HashMap<>();

	public JCFUserService() {
		map.put(UUID.randomUUID(), new User());
		map.put(UUID.randomUUID(), new User());
		map.put(UUID.randomUUID(), new User());
		map.put(UUID.randomUUID(), new User());
	}

	@Override
	public User createUser() {
		User user = new User();
		map.put(user.getId(), user);
		return user;
	}

	@Override
	public Optional<User> findById(UUID uuid) {
		return Optional.ofNullable(map.get(uuid));
	}

	@Override
	public List<User> findByAll() {
		return new ArrayList<>(map.values());
	}

	@Override
	public User updateUser(UUID uuid) {
		User user = map.get(uuid);
		user.update();
		return user;
	}

	@Override
	public void deleteUser(UUID uuid) {
		map.remove(uuid);
	}
}
