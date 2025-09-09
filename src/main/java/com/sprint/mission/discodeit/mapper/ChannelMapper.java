package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.mapper.helper.ChannelHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ChannelHelper.class})
public interface ChannelMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "type", source = "type")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "description", source = "description")
  @Mapping(target = "participants", source = "channel", qualifiedByName = "getParticipants")
  @Mapping(target = "lastMessageAt", source = "channel", qualifiedByName = "getLastMessageAt")
  ChannelDTO toDto(Channel channel);

}
