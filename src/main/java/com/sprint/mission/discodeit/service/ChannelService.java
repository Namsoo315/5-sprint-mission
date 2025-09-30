package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import java.util.List;
import java.util.UUID;


public interface ChannelService {

  ChannelDTO createPublicChannel(PublicChannelCreateRequest request);

  ChannelDTO createPrivateChannel(PrivateChannelCreateRequest request);

  List<ChannelDTO> findAllByUserId(UUID userId);

  ChannelDTO updateChannel(UUID chanelId, ChannelUpdateRequest request);

  void deleteChannel(UUID channelId);
}
