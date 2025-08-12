package com.sprint.mission.discodeit.dto.readstatus;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReadStatusCreateRequest {
	private UUID userId;
	private UUID chanelId;
}
