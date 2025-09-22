package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException() {
    super(ErrorCode.USER_STATUS_NOT_FOUND);
  }

  public static UserStatusNotFoundException withMessage(String message) {
    UserStatusNotFoundException ex = new UserStatusNotFoundException();
    ex.addDetail("message", message);
    return ex;
  }
}
