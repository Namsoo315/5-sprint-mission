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
	private final UUID id;
	private final UUID userId;
	private final UUID channelId;

	private final Instant createdAt;
	private Instant updatedAt;
	private Instant lastReadAt;

	public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
		id = UUID.randomUUID();
		this.userId = userId;
		this.channelId = channelId;
		this.lastReadAt = lastReadAt;
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
	}

	public void update(Instant newLastReadAt) {
		boolean anyValueUpdated = false;
		if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
			this.lastReadAt = newLastReadAt;
			anyValueUpdated = true;
		}

		if (anyValueUpdated) {
			this.updatedAt = Instant.now();
		}
	}

	@Override
	public String toString() {
		return "ReadStatus{" +
			"ReadStatusId=" + id +
			", userId=" + userId +
			", channelId=" + channelId +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			'}';
	}
}
