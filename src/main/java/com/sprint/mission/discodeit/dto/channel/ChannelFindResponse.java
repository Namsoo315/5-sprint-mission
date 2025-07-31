package com.sprint.mission.discodeit.dto.channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.ChannelType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChannelFindResponse {
	private UUID channelId;
	private ChannelType type;
	private String name;
	private String description;
	private Instant lastMessageTime;
	private List<UUID> participantsUserIds;
}
