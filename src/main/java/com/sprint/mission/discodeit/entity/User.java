package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class User{
	private final UUID userId;
	private String username;
	private int age;
	private final Long createdAt;
	private Long updatedAt;

	public User(String name, int age) {
		this.userId =UUID.randomUUID();
		this.username = name;
		this.age = age;
		this.createdAt = System.currentTimeMillis();
		this.updatedAt = createdAt;
	}

	public UUID getId() {
		return userId;
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

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void update(String username, int age){
		this.username = username;
		this.age = age;
		this.updatedAt = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final StringBuilder sb = new StringBuilder("User[");
		sb.append(" uuid=").append(userId);
		sb.append(", username=").append(username);
		sb.append(", age=").append(age);
		sb.append(", createdAt=").append(dateFormat.format(createdAt));
		sb.append(", updatedAt=").append(dateFormat.format(updatedAt));
		sb.append(" ]");
		return sb.toString();
	}
}
