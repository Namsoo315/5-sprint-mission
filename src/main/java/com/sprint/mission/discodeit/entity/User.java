package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
	private final UUID id;
	private String username;
	private int age;
	private final Long createdAt;
	private Long updatedAt;

	public User(String name, int age) {
		this.id =UUID.randomUUID();
		this.username = name;
		this.age = age;
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

	public String getUsername() {
		return username;
	}

	public int getAge() {
		return age;
	}

	public void update(String username, int age){
		this.username = username;
		this.age = age;
		this.updatedAt = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("User[");
		sb.append(" uuid=").append(id);
		sb.append(", username=").append(username);
		sb.append(", age=").append(age);
		sb.append(", createdAt=").append(createdAt);
		sb.append(", updatedAt=").append(updatedAt);
		sb.append(" ]");
		return sb.toString();
	}
}
