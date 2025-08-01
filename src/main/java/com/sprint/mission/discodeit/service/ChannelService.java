package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
;

public interface ChannelService {
	Channel createPublicChannel(PublicChannelCreateRequest request);

	Channel createPrivateChannel(PrivateChannelCreateRequest request);

	Optional<ChannelFindResponse> findByChannelId(UUID channelId);

	List<Channel> findByChannelName(String name);

	List<ChannelFindResponse> findAllByUserId(UUID userId);

	void updateChannel(ChannelUpdateRequest request);

	void deleteChannel(UUID uuid);
}
