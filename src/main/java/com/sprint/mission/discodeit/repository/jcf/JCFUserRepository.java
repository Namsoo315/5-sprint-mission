package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

public class JCFUserRepository implements UserRepository{
	private final Map<UUID, User> map = new HashMap<>();

	public JCFUserRepository() {
		User user1 = new User("남현수1", 10);
		map.put(user1.getId(), user1);

		User user2 = new User("남현수2", 20);
		map.put(user2.getId(), user2);

		User user3 = new User("남현수3", 30);
		map.put(user3.getId(), user3);
	}

	@Override
	public User createUser(String name, int age) {
		User user = new User(name, age);
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
