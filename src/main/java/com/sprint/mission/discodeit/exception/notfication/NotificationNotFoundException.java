package com.sprint.mission.discodeit.exception.notfication;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;

public class NotificationNotFoundException extends NotificationException {

  public NotificationNotFoundException() {
    super(ErrorCode.NOTIFICATION_NOT_FOUND);
  }

  public static NotificationNotFoundException withMessage(String message) {
    NotificationNotFoundException ex = new NotificationNotFoundException();
    ex.addDetail("message", message);
    return ex;
  }
}
