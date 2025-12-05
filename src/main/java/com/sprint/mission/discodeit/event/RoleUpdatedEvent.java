package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;

public record RoleUpdatedEvent(
    User user,
    UserRole oldRole,
    UserRole newRole
) {

}
