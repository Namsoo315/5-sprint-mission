package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException() {
    super(ErrorCode.READ_STATUS_NOT_FOUND);
  }

  public static ReadStatusNotFoundException withMessage(String message) {
    ReadStatusNotFoundException ex = new ReadStatusNotFoundException();
    ex.addDetail("message", message);
    return ex;
  }
}
