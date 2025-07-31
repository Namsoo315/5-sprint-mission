package com.sprint.mission.discodeit.dto;

import java.util.UUID;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserFindResponse {
	private UUID userId;
	private String username;
	private String email;
	private boolean status;

	public UserFindResponse(User user, UserStatus status) {
		this.userId = user.getUserId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.status = status != null && status.isOnline();
	}
}
