package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChannelCreateResponse {
	private UUID channelId;
	private ChannelType channelType;

}
