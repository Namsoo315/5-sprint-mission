package com.sprint.mission.discodeit.dto.userstatus;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserStatusCreateRequest {
	private UUID userId;
}
