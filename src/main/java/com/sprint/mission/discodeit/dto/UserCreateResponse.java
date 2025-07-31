package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserCreateResponse {
	private String username;
	private String email;
	private UserStatus status;
	private BinaryContent content;

	@Override
	public String toString() {
		return "UserCreateResponse{" +
			"username='" + username + '\'' +
			", email='" + email + '\'' +
			", status=" + status +
			", content=" + content +
			'}';
	}
}
