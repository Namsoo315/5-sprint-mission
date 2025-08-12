package com.sprint.mission.discodeit.dto.channel;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PrivateChannelCreateRequest {
	private List<UUID> participantsUserIds;
}
