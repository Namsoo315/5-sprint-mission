package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserFindResponse {
	private UUID userId;
	private String username;
	private String email;
	private boolean status;

	@Override
	public String toString() {
		return "UserFindResponse{" +
			"userId=" + userId +
			", username='" + username + '\'' +
			", email='" + email + '\'' +
			", status=" + status +
			'}';
	}
}
