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

	private final UUID ReadStatusId;
	private final UUID userId;
	private final UUID channelId;
	private Instant createdAt;
	private Instant updatedAt;

	public ReadStatus(UUID userId, UUID channelId) {
		ReadStatusId = UUID.randomUUID();
		this.userId = userId;
		this.channelId = channelId;
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
	}

	@Override
	public String toString() {
		return "ReadStatus{" +
			"ReadStatusId=" + ReadStatusId +
			", userId=" + userId +
			", channelId=" + channelId +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			'}';
	}
}
