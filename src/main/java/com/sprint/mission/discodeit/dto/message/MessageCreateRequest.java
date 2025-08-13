package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageCreateRequest(
	UUID userId,
	UUID channelId,
	String message
) {}
