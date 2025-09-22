package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserStatusSaveFailedException extends UserStatusException {

  public UserStatusSaveFailedException() {
    super(ErrorCode.USER_STATUS_SAVE_FAILED);
  }

  public static UserStatusSaveFailedException withMessage(String message) {
    UserStatusSaveFailedException ex = new UserStatusSaveFailedException();
    ex.addDetail("message", message);
    return ex;
  }
}