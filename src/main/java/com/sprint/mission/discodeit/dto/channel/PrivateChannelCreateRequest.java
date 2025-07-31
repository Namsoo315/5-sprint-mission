package com.sprint.mission.discodeit.dto.channel;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrivateChannelCreateRequest {
	private String name;
	private String description;
	private List<UUID> participantsUserIds;
}
