package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException() {
    super(ErrorCode.MESSAGE_NOT_FOUND);
  }

  public static MessageNotFoundException withMessage(String message) {
    MessageNotFoundException ex = new MessageNotFoundException();
    ex.addDetail("message", message);
    return ex;
  }
}
