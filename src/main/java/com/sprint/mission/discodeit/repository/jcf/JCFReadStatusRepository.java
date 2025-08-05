package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

@Repository("readStatusRepository")
public class JCFReadStatusRepository implements ReadStatusRepository {
	private final Map<UUID, ReadStatus> map = new HashMap<>();

	@Override
	public ReadStatus save(ReadStatus readStatus) {
		boolean isNew = !existsById(readStatus.getReadStatusId());

		map.put(readStatus.getReadStatusId(), readStatus);

		if (isNew) {
			System.out.println("readStatus가 생성 되었습니다.");
		} else {
			System.out.println("readStatus가 업데이트 되었습니다.");
		}
		return readStatus;
	}

	@Override
	public Optional<ReadStatus> findById(UUID readStatusId) {
		if (existsById(readStatusId)) {
			return Optional.of(map.get(readStatusId));
		}

		return Optional.empty();
	}

	@Override
	public List<ReadStatus> findAll() {
		return new ArrayList<>(map.values());
	}

	@Override
	public List<ReadStatus> findAllByChannelId(UUID channelId) {
		for (ReadStatus readStatus : map.values()) {
			if (readStatus.getChannelId().equals(channelId)) {
				return List.of(readStatus);
			}
		}
		return new ArrayList<>();
	}

	@Override
	public List<ReadStatus> findAllByUserId(UUID userId) {
		for (ReadStatus readStatus : map.values()) {
			if (readStatus.getUserId().equals(userId)) {
				return List.of(readStatus);
			}
		}

		return new ArrayList<>();
	}

	@Override
	public void delete(UUID readStatusId) {
		map.remove(readStatusId);
	}

	@Override
	public void deleteByChannelId(UUID channelId) {
		for (ReadStatus readStatus : map.values()) {
			if (readStatus.getChannelId().equals(channelId)) {
				map.remove(readStatus.getReadStatusId());
			}
		}
	}

	@Override
	public boolean existsById(UUID readStatusId) {
		return map.containsKey(readStatusId);
	}
}
