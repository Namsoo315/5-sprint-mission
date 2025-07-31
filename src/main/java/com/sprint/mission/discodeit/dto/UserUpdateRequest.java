package com.sprint.mission.discodeit.dto;

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
public class UserUpdateRequest {
	private UUID userId;
	private String username;
	private String email;
	private String password;
	private BinaryContent content;
}
