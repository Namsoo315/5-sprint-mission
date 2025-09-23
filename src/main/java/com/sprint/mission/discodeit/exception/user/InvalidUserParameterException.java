package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidUserParameterException extends UserException {

  public InvalidUserParameterException() {
    super(ErrorCode.INVALID_USER_PARAMETER);
  }

  public static InvalidUserParameterException withMessage(String message) {
    InvalidUserParameterException exception = new InvalidUserParameterException();
    exception.addDetail("message", message);
    return exception;
  }
}
