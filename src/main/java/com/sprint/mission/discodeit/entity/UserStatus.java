package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class UserStatus implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private final UUID UserStatusId;
	private final UUID userId;

	private final Instant createdAt;
	private Instant updatedAt;
	private Instant lastActiveAt;

	public UserStatus(UUID userId, Instant lastReadAt) {
		this.UserStatusId = UUID.randomUUID();
		this.userId = userId;
		this.lastActiveAt = lastReadAt;
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
	}

	public void updateStatus(Instant lastActivateAt) {
		boolean anyValueUpdated = false;
		if (lastActivateAt != null && !lastActivateAt.equals(this.lastActiveAt)) {
			this.lastActiveAt = lastActivateAt;
			anyValueUpdated = true;
		}

		if (anyValueUpdated) {
			this.updatedAt = Instant.now();
		}

	}

	// 현재 시간 기준 5분 이내에 업데이트된 경우 온라인으로 간주;
	public boolean isOnline() {
		Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

		return lastActiveAt.isAfter(instantFiveMinutesAgo);
	}

	@Override
	public String toString() {
		return "UserStatus{" +
			"UserStatusId=" + UserStatusId +
			", userId=" + userId +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			'}';
	}
}
