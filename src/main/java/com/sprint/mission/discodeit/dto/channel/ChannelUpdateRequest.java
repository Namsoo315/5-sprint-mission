package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChannelUpdateRequest {
	private UUID channelId;
	private String name;
	private String description;
}
