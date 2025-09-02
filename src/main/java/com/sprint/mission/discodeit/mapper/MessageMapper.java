package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "createdAt", source = "createdAt")
  @Mapping(target = "updatedAt", source = "updatedAt")
  @Mapping(target = "content", source = "content")
  @Mapping(target = "channelId", source = "channel.id")
  @Mapping(target = "author", source = "author")
  @Mapping(target = "attachments", source = "attachments")
  MessageDTO toDto(Message message);

  List<MessageDTO> toDto(List<Message> message);
}
