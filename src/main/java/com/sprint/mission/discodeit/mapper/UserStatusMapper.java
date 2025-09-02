package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "lastActiveAt", source = "lastActiveAt")
  UserStatusDto toDto(UserStatus userStatus);

  List<UserStatusDto> toDto(List<UserStatus> userStatus);
}
