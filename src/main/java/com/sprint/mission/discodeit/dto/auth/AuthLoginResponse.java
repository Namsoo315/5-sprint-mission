package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthLoginResponse {
	private User user;
}
