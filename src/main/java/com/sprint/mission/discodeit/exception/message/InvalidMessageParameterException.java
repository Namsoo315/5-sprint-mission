package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidMessageParameterException extends MessageException {

  public InvalidMessageParameterException() {
    super(ErrorCode.INVALID_MESSAGE_PARAMETER);
  }

  public static InvalidMessageParameterException withMessage(String message) {
    InvalidMessageParameterException ex = new InvalidMessageParameterException();
    ex.addDetail("message", message);
    return ex;
  }
}
