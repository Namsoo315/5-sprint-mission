package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

// 중복 채널 예외
public class DuplicateChannelException extends ChannelException {

  public DuplicateChannelException() {
    super(ErrorCode.DUPLICATE_CHANNEL);
  }

  public static DuplicateChannelException withMessage(String message) {
    DuplicateChannelException exception = new DuplicateChannelException();
    exception.addDetail("message", message);
    return exception;
  }
}
