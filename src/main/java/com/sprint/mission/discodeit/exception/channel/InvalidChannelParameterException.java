package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

// 잘못된 파라미터 예외
public class InvalidChannelParameterException extends ChannelException {

  public InvalidChannelParameterException() {
    super(ErrorCode.INVALID_CHANNEL_PARAMETER);
  }

  public static InvalidChannelParameterException withMessage(String message) {
    InvalidChannelParameterException exception = new InvalidChannelParameterException();
    exception.addDetail("message", message);
    return exception;
  }
}
