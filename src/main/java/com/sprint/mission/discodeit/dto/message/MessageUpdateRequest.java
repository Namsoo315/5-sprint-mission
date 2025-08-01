package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageUpdateRequest {
	private UUID messageId;
	private String newContent;
}
