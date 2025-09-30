package com.sprint.mission.discodeit.exception.user;


import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidUserCredentialsException extends UserException {

  public InvalidUserCredentialsException() {
    super(ErrorCode.INVALID_USER_CREDENTIALS);
  }

  public static InvalidUserCredentialsException wrongUsername(String username) {
    InvalidUserCredentialsException exception = new InvalidUserCredentialsException();
    exception.addDetail("username", username); // 이거는 굳이 보여줘야 할까?
    return exception;
  }

  public static InvalidUserCredentialsException wrongPassword(String password) {
    InvalidUserCredentialsException exception = new InvalidUserCredentialsException();
    exception.addDetail("password", password); // 이거는 굳이 보여줘야 할까?
    return exception;
  }

  public static InvalidUserCredentialsException withMessage(String message) {
    InvalidUserCredentialsException exception = new InvalidUserCredentialsException();
    exception.addDetail("message", message);
    return exception;
  }
} 