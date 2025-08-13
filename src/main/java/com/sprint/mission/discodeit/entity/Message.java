package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
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
	private final List<UUID> attachmentIds;		// binaryContent

	private String content;

	private final Instant createdAt;
	private Instant updatedAt;

	public Message(UUID userId, UUID channelId, String content, List<UUID> attachmentIds) {
		this.messageId = UUID.randomUUID();
		this.userId = userId;
		this.channelId = channelId;
		this.content = content;
		this.attachmentIds = attachmentIds;
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
	}

	public void update(String message) {
		this.content = message;
		this.updatedAt = Instant.now();
	}

	@Override
	public String toString() {
		return "Message{" +
			"messageId=" + messageId +
			", userId=" + userId +
			", channelId=" + channelId +
			", attachmentIds=" + attachmentIds +
			", message='" + content + '\'' +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			'}';
	}
}
