package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChannelMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "type", source = "type")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "description", source = "description")
  @Mapping(target = "participants", ignore = true)
  @Mapping(target = "lastMessageAt", ignore = true)
  ChannelDto toDto(Channel channel);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", source = "name")
  @Mapping(target = "description", source = "description")
  @Mapping(target = "type", constant = "PUBLIC")
// ✅ 항상 PUBLIC으로 매핑
  Channel toPublicEntity(PublicChannelCreateRequest request);
}
