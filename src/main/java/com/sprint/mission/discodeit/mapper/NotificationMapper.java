package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.NotificationDTO;
import com.sprint.mission.discodeit.entity.Notification;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "createdAt", source = "createdAt")
  @Mapping(target = "receiverId", source = "user.id")
  @Mapping(target = "content", source = "content")
  @Mapping(target = "title", source = "title")
  NotificationDTO toDto(Notification notification);

  List<NotificationDTO> toDto(List<Notification> notifications);
}
