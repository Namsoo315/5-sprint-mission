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
		map.put(UUID.randomUUID(), new User("남현수1", 10));
		map.put(UUID.randomUUID(), new User("남현수2", 20));
		map.put(UUID.randomUUID(), new User("남현수3", 30));
		map.put(UUID.randomUUID(), new User("남현수4", 40));
	}

	@Override
	public User createUser() {
		User user = new User("남현수5", 50);
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
	public void updateUser(UUID uuid, String name, int age) {
		User user = map.get(uuid);
		user.update(name, age);
	}

	@Override
	public void deleteUser(UUID uuid) {
		map.remove(uuid);
	}
}
