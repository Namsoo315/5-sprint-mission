package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

// Private 채널 관련 예외
public class PrivateChannelNotFoundException extends ChannelException {

  public PrivateChannelNotFoundException() {
    super(ErrorCode.PRIVATE_CHANNEL_NOT_FOUND);
  }

  public static PrivateChannelNotFoundException withMessage(String message) {
    PrivateChannelNotFoundException exception = new PrivateChannelNotFoundException();
    exception.addDetail("message", message);
    return exception;
  }
}
