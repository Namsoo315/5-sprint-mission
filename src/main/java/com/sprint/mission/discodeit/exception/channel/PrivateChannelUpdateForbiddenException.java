package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

// Private 채널 수정 금지 예외
public class PrivateChannelUpdateForbiddenException extends ChannelException {

  public PrivateChannelUpdateForbiddenException() {
    super(ErrorCode.PRIVATE_CHANNEL_UPDATE_FORBIDDEN);
  }

  public static PrivateChannelUpdateForbiddenException withMessage(String message) {
    PrivateChannelUpdateForbiddenException exception = new PrivateChannelUpdateForbiddenException();
    exception.addDetail("message", message);
    return exception;
  }
}
