package com.sprint.mission.discodeit.exception.user;


import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidCredentialsException extends UserException {

  public InvalidCredentialsException() {
    super(ErrorCode.INVALID_USER_CREDENTIALS);
  }

  public static InvalidCredentialsException wrongUsername(String username) {
    InvalidCredentialsException exception = new InvalidCredentialsException();
    exception.addDetail("username", username); // 이거는 굳이 보여줘야 할까?
    return exception;
  }

  public static InvalidCredentialsException wrongPassword(String password) {
    InvalidCredentialsException exception = new InvalidCredentialsException();
    exception.addDetail("password", password); // 이거는 굳이 보여줘야 할까?
    return exception;
  }

  public static InvalidCredentialsException withMessage(String message) {
    InvalidCredentialsException exception = new InvalidCredentialsException();
    exception.addDetail("message", message);
    return exception;
  }
} 