package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class Message implements Serializable {
	@Serial
	private final long serialVersionUID = 1L;
	private UUID messageId;
	private final UUID userId;
	private final UUID channelId;
	private String message;
	private final Long createdAt;
	private Long updatedAt;

	public Message(UUID userId, UUID channelId, String message) {
		this.messageId = UUID.randomUUID();
		this.userId = userId;
		this.channelId = channelId;
		this.message = message;
		this.createdAt = System.currentTimeMillis();
		this.updatedAt = createdAt;
	}

	public long getSerialVersionUID() {
		return serialVersionUID;
	}

	public UUID getMessageId() {
		return messageId;
	}

	public UUID getUserId() {
		return userId;
	}

	public UUID getChannelId() {
		return channelId;
	}

	public String getMessage() {
		return message;
	}

	public Long getCreatedAt() {return createdAt;}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void update(String message) {
		this.message = message;
		this.updatedAt = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final StringBuffer sb = new StringBuffer("Message{");
		sb.append("messageId=").append(messageId);
		sb.append(", userId=").append(userId);
		sb.append(", channelId=").append(channelId);
		sb.append(", message='").append(message).append('\'');
		sb.append(", createdAt=").append(dateFormat.format(createdAt));
		sb.append(", updatedAt=").append(dateFormat.format(updatedAt));
		sb.append('}');
		return sb.toString();
	}
}
