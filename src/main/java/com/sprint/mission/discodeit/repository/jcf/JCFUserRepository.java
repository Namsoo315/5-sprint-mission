package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

public class JCFUserRepository implements UserRepository {
	private final Map<UUID, User> map = new HashMap<>();

	@Override
	public User save(User user) {
		boolean isNew = !existsById(user.getUserId());

		map.put(user.getUserId(), user);

		if (isNew) {
			System.out.println("생성 되었습니다.");
		} else {
			System.out.println("업데이트 되었습니다.");
		}

		return user;
	}

	@Override
	public Optional<User> findById(UUID id) {
		if (existsById(id)) {
			return Optional.of(map.get(id));
		}

		return Optional.empty();
	}

	@Override
	public Optional<User> findByUsername(String username) {
		for (User user : map.values()) {
			if (user.getUsername().equals(username)) {
				return Optional.of(user);
			}
		}

		return Optional.empty();
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(map.values());
	}

	@Override
	public long count() {
		return map.size();
	}

	@Override
	public void delete(UUID id) {
		if (!existsById(id)) {
			throw new IllegalArgumentException("일치하는 ID가 없습니다.");
		}
		map.remove(id);
		System.out.println(id + " 유저가 삭제 되었습니다.");
	}

	@Override
	public boolean existsById(UUID id) {
		return map.containsKey(id);
	}
}
