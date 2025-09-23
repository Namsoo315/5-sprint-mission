package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

// Private 채널 관련 예외
public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException() {
    super(ErrorCode.CHANNEL_NOT_FOUND);
  }

  public static ChannelNotFoundException withMessage(String message) {
    ChannelNotFoundException exception = new ChannelNotFoundException();
    exception.addDetail("message", message);
    return exception;
  }
}
