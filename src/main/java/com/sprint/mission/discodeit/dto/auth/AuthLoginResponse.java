package com.sprint.mission.discodeit.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AuthLoginResponse {
	private String username;
	private String email;

	@Override
	public String toString() {
		return "AuthLoginResponse{" +
			"username='" + username + '\'' +
			", email='" + email + '\'' +
			'}';
	}
}
