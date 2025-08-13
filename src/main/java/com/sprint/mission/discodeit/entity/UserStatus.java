package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class UserStatus implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private final UUID UserStatusId;
	private final UUID userId;

	private boolean status;	// Online = true, Offline = false;

	private final Instant createdAt;
	private Instant updatedAt;

	public UserStatus(UUID userId) {
		this.UserStatusId = UUID.randomUUID();
		this.userId = userId;
		this.status = true;	// 생성 시 온라인상태로 가야한다
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
	}

	public void updateStatus () {
		this.status = true;
		this.updatedAt = Instant.now();
	}

	// 현재 시간 기준 5분 이내에 업데이트된 경우 온라인으로 간주;
	public boolean isOnline() {
		return updatedAt.isBefore(Instant.now().minusSeconds(300));
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
