package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidParameterException extends UserException {

  public InvalidParameterException() {
    super(ErrorCode.INVALID_USER_PARAMETER);
  }

  public static InvalidParameterException withMessage(String message) {
    InvalidParameterException exception = new InvalidParameterException();
    exception.addDetail("message", message);
    return exception;
  }
}
