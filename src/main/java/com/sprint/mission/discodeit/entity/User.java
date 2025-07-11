package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
	private UUID id;
	private Long createdAt;
	private Long updatedAt;

	public User() {
		this.id =UUID.randomUUID();
		this.createdAt = System.currentTimeMillis();
	}

	public UUID getId() {
		return id;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void update(){
		this.updatedAt = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("User[");
		sb.append(" uuid=").append(id);
		sb.append(", createdAt=").append(createdAt);
		sb.append(", updatedAt=").append(updatedAt);
		sb.append(" ]");
		return sb.toString();
	}
}
