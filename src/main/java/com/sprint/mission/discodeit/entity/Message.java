package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class Message{
	private UUID userId;
	private UUID channelId;
	private String message;
	private Long createdAt;
	private Long updatedAt;

	public Message(UUID userId, UUID channelId, String message) {
		this.userId = userId;
		this.channelId = channelId;
		this.message = message;
		this.createdAt = System.currentTimeMillis();
		this.updatedAt = createdAt;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public UUID getChannelId() {
		return channelId;
	}

	public void setChannelId(UUID channelId) {
		this.channelId = channelId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void update(String message) {
		this.message = message;
		this.updatedAt = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final StringBuffer sb = new StringBuffer("Message{");
		sb.append("userId=").append(userId);
		sb.append(", channelId=").append(channelId);
		sb.append(", message='").append(message).append('\'');
		sb.append(", createdAt=").append(dateFormat.format(createdAt));
		sb.append(", updatedAt=").append(dateFormat.format(updatedAt));
		sb.append('}');
		return sb.toString();
	}
}
