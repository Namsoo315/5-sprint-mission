package com.sprint.mission.discodeit.repository.jcf;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

@Repository("messageRepository")
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFMessageRepository implements MessageRepository {
	private final Map<UUID, Message> map = new HashMap<>();

	@Override
	public Message save(Message message) {
		boolean isNew = !existsById(message.getMessageId());
		map.put(message.getMessageId(), message);

		if (isNew) {
			System.out.println("message가 생성 되었습니다.");
		} else {
			System.out.println("message가 업데이트 되었습니다.");
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
		for (Message message : map.values()) {
			if (message.getChannelId().equals(channelId)) {
				return List.of(message);
			}
		}

		return new ArrayList<>();
	}

	@Override
	public Instant LatestMessageByChannelId(UUID channelId) {
		return this.findAll().stream()
			.filter(message -> message.getChannelId().equals(channelId))
			.max(Comparator.comparing(Message::getCreatedAt))
			.map(Message::getCreatedAt)
			.orElse(null);
	}

	@Override
	public void delete(UUID messageId) {
		if (!existsById(messageId)) {
			throw new IllegalArgumentException("일치하는 ID가 없습니다.");
		}
		map.remove(messageId);
		System.out.println(messageId + " 가 삭제 되었습니다.");
	}

	@Override
	public void deleteByChannelId(UUID channelId) {
		
	}

	@Override
	public boolean existsById(UUID messageId) {
		return map.containsKey(messageId);
	}
}
