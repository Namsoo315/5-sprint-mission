package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MessageUpdateRequest {
	private UUID messageId;
	private String newContent;
}
