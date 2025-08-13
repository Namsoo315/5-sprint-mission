package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
@Repository
public class JCFUserRepository implements UserRepository {
	private final Map<UUID, User> map = new HashMap<>();

	@Override
	public User save(User user) {
		boolean isNew = !existsById(user.getUserId());

		map.put(user.getUserId(), user);

		if (isNew) {
			System.out.println("user가 생성 되었습니다." + user.getUserId());
		} else {
			System.out.println("user가 업데이트 되었습니다." + user.getUserId());
		}

		return user;
	}

	@Override
	public Optional<User> findById(UUID userId) {
		if (existsById(userId)) {
			return Optional.of(map.get(userId));
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
	public Optional<User> findByEmail(String email) {
		for (User user : map.values()) {
			if (user.getEmail().equals(email)) {
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
	public void delete(UUID userId) {
		if (!existsById(userId)) {
			throw new IllegalArgumentException("일치하는 ID가 없습니다.");
		}
		map.remove(userId);
		System.out.println(userId + " 유저가 삭제 되었습니다.");
	}

	@Override
	public boolean existsById(UUID userId) {
		return map.containsKey(userId);
	}
}
