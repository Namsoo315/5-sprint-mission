package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "channelId", source = "channel.id")
  @Mapping(target = "lastReadAt", source = "lastReadAt")
  @Mapping(target = "notificationEnabled", source = "notificationEnabled")
  ReadStatusDTO toDto(ReadStatus readStatus);

  List<ReadStatusDTO> toDto(List<ReadStatus> users);
}
