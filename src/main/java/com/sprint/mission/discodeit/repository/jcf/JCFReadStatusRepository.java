package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {

  private final Map<UUID, ReadStatus> map = new HashMap<>();

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    boolean isNew = !existsById(readStatus.getId());

    map.put(readStatus.getId(), readStatus);

    if (isNew) {
      System.out.println("readStatus가 생성 되었습니다. : " + readStatus.getId());
    } else {
      System.out.println("readStatus가 업데이트 되었습니다." + readStatus.getId());
    }
    return readStatus;
  }

  @Override
  public Optional<ReadStatus> findByReadStatusId(UUID readStatusId) {
    if (existsById(readStatusId)) {
      return Optional.of(map.get(readStatusId));
    }

    return Optional.empty();
  }

  @Override
  public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
    return this.findAll().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId) && readStatus.getChannelId()
            .equals(channelId))
        .findFirst();
  }

  @Override
  public List<ReadStatus> findAll() {
    return new ArrayList<>(map.values());
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return this.findAll().stream().filter(status -> status.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return this.findAll().stream().filter(status -> status.getUserId().equals(userId)).toList();
  }

  @Override
  public void delete(UUID readStatusId) {
    map.remove(readStatusId);
  }

  @Override
  public void deleteByChannelId(UUID channelId) {
    map.values().removeIf(readStatus -> readStatus.getChannelId().equals(channelId));
  }

  @Override
  public boolean existsById(UUID readStatusId) {
    return map.containsKey(readStatusId);
  }
}
