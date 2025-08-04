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
	private UUID profileId;		// binaryContent Id

	private String username;
	private String email;
	private String password;

	private final Instant createdAt;
	private Instant updatedAt;

	public User(String name, String email, String password, UUID profileId) {
		this.userId = UUID.randomUUID();
		this.username = name;
		this.email = email;
		this.password = password;
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
		this.profileId = profileId;
	}

	public void update(String username, String email, String age, UUID profileId) {
		this.username = username;
		this.email = email;
		this.password = age;
		this.updatedAt = Instant.now();
		this.profileId = profileId;
	}

	@Override
	public String toString() {
		return "User{" +
			"userId=" + userId +
			", username='" + username + '\'' +
			", email='" + email + '\'' +
			", password='" + password + '\'' +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			", profileId=" + profileId +
			'}';
	}
}
