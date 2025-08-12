package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChannelUpdateRequest {
	private UUID channelId;
	private String name;
	private String description;
}
