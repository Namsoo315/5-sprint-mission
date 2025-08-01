package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Message implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private final UUID messageId;
	private final UUID userId;
	private final UUID channelId;
	private String message;
	private final Instant createdAt;
	private Instant updatedAt;

	public Message(UUID userId, UUID channelId, String message) {
		this.messageId = UUID.randomUUID();
		this.userId = userId;
		this.channelId = channelId;
		this.message = message;
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
	}

	public void update(String message) {
		this.message = message;
		this.updatedAt = Instant.now();
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Message{");
		sb.append("messageId=").append(messageId).append('\'');
		sb.append(", userId=").append(userId).append('\'');
		sb.append(", channelId=").append(channelId).append('\'');
		sb.append(", message='").append(message).append('\'');
		sb.append(", createdAt=").append(createdAt).append('\'');
		sb.append(", updatedAt=").append(updatedAt);
		sb.append('}');
		return sb.toString();
	}
}
