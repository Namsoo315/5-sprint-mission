package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class User implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private final UUID userId;
	private String username;
	private String email;
	private String password;
	private final Instant createdAt;
	private Instant updatedAt;

	public User(String name, String email, String password) {
		this.userId = UUID.randomUUID();
		this.username = name;
		this.email = email;
		this.password = password;
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
	}

	public void update(String username, String email, String age) {
		this.username = username;
		this.email = email;
		this.password = age;
		this.updatedAt = Instant.now();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("User[");
		sb.append(" uuid=").append(userId).append('\'');
		sb.append(", username=").append(username).append('\'');
		sb.append(", email=").append(email).append('\'');
		sb.append(", age=").append(password).append('\'');
		sb.append(", createdAt=").append(createdAt).append('\'');
		sb.append(", updatedAt=").append(updatedAt);
		sb.append(" ]");
		return sb.toString();
	}
}
