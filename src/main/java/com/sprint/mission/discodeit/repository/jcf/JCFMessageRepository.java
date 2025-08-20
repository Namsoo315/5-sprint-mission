package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
@Repository
public class JCFMessageRepository implements MessageRepository {

  private final Map<UUID, Message> map = new HashMap<>();

  @Override
  public Message save(Message message) {
    boolean isNew = !existsById(message.getId());
    map.put(message.getId(), message);

    if (isNew) {
      System.out.println("message가 생성 되었습니다." + message.getId());
    } else {
      System.out.println("message가 업데이트 되었습니다." + message.getId());
    }
    return message;
  }

  @Override
  public Optional<Message> findById(UUID messageId) {
    if (existsById(messageId)) {
      return Optional.of(map.get(messageId));
    }

    return Optional.empty();
  }

  @Override
  public List<Message> findAll() {
    return new ArrayList<>(map.values());
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return map.values().stream().filter(message -> message.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public Optional<Message> latestMessageByChannelId(UUID channelId) {
    return this.findAll().stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .max(Comparator.comparing(Message::getCreatedAt));
  }

  @Override
  public void delete(UUID messageId) {
    if (!existsById(messageId)) {
      throw new NoSuchElementException("존재하지 않는 회원입니다.");
    }
    map.remove(messageId);
  }

  @Override
  public void deleteByChannelId(UUID channelId) {
    map.values().removeIf(message -> message.getChannelId().equals(channelId));
  }

  @Override
  public boolean existsById(UUID messageId) {
    return map.containsKey(messageId);
  }
}
