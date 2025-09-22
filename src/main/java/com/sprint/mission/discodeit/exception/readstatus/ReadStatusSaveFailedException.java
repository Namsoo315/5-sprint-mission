package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class ReadStatusSaveFailedException extends ReadStatusException {

  public ReadStatusSaveFailedException() {
    super(ErrorCode.READ_STATUS_SAVE_FAILED);
  }

  public static ReadStatusSaveFailedException withMessage(String message) {
    ReadStatusSaveFailedException ex = new ReadStatusSaveFailedException();
    ex.addDetail("message", message);
    return ex;
  }
}