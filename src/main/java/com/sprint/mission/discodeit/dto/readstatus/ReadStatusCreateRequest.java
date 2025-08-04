package com.sprint.mission.discodeit.dto.readstatus;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReadStatusCreateRequest {
	private UUID userId;
	private UUID chanelId;
}
