package com.sprint.mission.discodeit.dto.readstatus;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReadStatusUpdateRequest {
	private UUID ReadStatusId;
	private UUID userId;
	private UUID chanelId;
}
