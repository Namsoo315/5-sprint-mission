package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

import com.sprint.mission.discodeit.entity.BinaryContent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserCreateRequest {
	private UUID userId;
	private String username;
	private String email;
	private String password;
	private BinaryContent content;

	public UserCreateRequest(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserCreateRequest{" +
			"userId=" + userId +
			", username='" + username + '\'' +
			", email='" + email + '\'' +
			", age=" + password +
			", content=" + content +
			'}';
	}
}
