package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class User implements Serializable {

	@Serial
	private final long serialVersionUID = 1L;
	private final UUID userId;
	private String username;
	private int age;
	private final Instant createdAt;
	private Instant updatedAt;

	public User(String name, int age) {
		this.userId = UUID.randomUUID();
		this.username = name;
		this.age = age;
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
	}

	public void update(String username, int age){
		this.username = username;
		this.age = age;
		this.updatedAt = Instant.now();
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final StringBuilder sb = new StringBuilder("User[");
		sb.append(" uuid=").append(userId).append('\'');
		sb.append(", username=").append(username).append('\'');
		sb.append(", age=").append(age).append('\'');
		sb.append(", createdAt=").append(createdAt).append('\'');
		sb.append(", updatedAt=").append(updatedAt);
		sb.append(" ]");
		return sb.toString();
	}
}
