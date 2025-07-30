package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class UserStatus {
	private final UUID UserStatusId;
	private final UUID userId;
	private boolean status;	// Online = true, Offline = false;
	private Instant createdAt;
	private Instant updatedAt;

	public UserStatus(UUID userStatusId, UUID userId) {
		UserStatusId = userStatusId;
		this.userId = userId;
		this.status = false;
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
	}

	public void updateStatus (boolean status) {
		this.status = true;
		this.updatedAt = Instant.now();
	}

	// 현재 시간 기준 5분 이내에 업데이트된 경우 온라인으로 간주;
	public boolean isOnline() {
		return updatedAt.isAfter(Instant.now().minusSeconds(300));
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
