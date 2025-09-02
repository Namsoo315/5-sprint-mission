package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.helper.UserStatusHelper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserStatusHelper.class})
public interface UserMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "username", source = "username")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "profile", source = "profile")
  @Mapping(target = "online", source = "user", qualifiedByName = "getOnlineStatus")
  UserDTO toDto(User user);

  List<UserDTO> toDto(List<User> users);
}