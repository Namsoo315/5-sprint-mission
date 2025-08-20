package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
@Repository
public class JCFUserStatusRepository implements UserStatusRepository {

  private final Map<UUID, UserStatus> map = new HashMap<>();

  @Override
  public UserStatus save(UserStatus userStatus) {
    boolean isNew = !existsById(userStatus.getId());

    map.put(userStatus.getId(), userStatus);

    if (isNew) {
      System.out.println("userStatus가 생성 되었습니다." + userStatus.getId());
    } else {
      System.out.println("userStatus가 업데이트 되었습니다." + userStatus.getId());
    }
    return userStatus;
  }

  @Override
  public Optional<UserStatus> findById(UUID userStatusId) {
    if (existsById(userStatusId)) {
      return Optional.of(map.get(userStatusId));
    }

    return Optional.empty();
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    for (UserStatus userStatus : map.values()) {
      if (userStatus.getUserId().equals(userId)) {
        return Optional.of(userStatus);
      }
    }
    return Optional.empty();
  }

  @Override
  public List<UserStatus> findAll() {
    return new ArrayList<>(map.values());
  }

  @Override
  public void delete(UUID userStatusId) {
    map.remove(userStatusId);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    map.values().removeIf(userStatus -> userStatus.getUserId().equals(userId));
  }

  @Override
  public boolean existsById(UUID userStatusId) {
    return map.containsKey(userStatusId);
  }
}
