package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class ReadStatus implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private final UUID readStatusId;
	private final UUID userId;
	private final UUID channelId;

	private boolean isRead;

	private final Instant createdAt;
	private Instant updatedAt;

	public ReadStatus(UUID userId, UUID channelId) {
		readStatusId = UUID.randomUUID();
		this.userId = userId;
		this.channelId = channelId;
		this.isRead = false;
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
	}

	public void update() {
		this.isRead = true;
		this.updatedAt = Instant.now();
	}

	@Override
	public String toString() {
		return "ReadStatus{" +
			"ReadStatusId=" + readStatusId +
			", userId=" + userId +
			", channelId=" + channelId +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			'}';
	}
}
