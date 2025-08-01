package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MessageCreateRequest {
	private UUID userId;
	private UUID channelId;
	private String message;
	private List<UUID> attachmentIds;
}
