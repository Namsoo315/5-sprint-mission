package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class PublicChannelNotFoundException extends ChannelException {

  public PublicChannelNotFoundException() {
    super(ErrorCode.PUBLIC_CHANNEL_NOT_FOUND);
  }

  public static PublicChannelNotFoundException withMessage(String message) {
    PublicChannelNotFoundException exception = new PublicChannelNotFoundException();
    exception.addDetail("message", message);
    return exception;
  }
}
