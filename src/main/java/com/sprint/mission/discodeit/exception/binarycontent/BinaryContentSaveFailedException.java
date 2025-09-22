package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class BinaryContentSaveFailedException extends BinaryContentException {

  public BinaryContentSaveFailedException() {
    super(ErrorCode.BINARY_CONTENT_SAVE_FAILED);
  }

  public static BinaryContentSaveFailedException withMessage(String message) {
    BinaryContentSaveFailedException ex = new BinaryContentSaveFailedException();
    ex.addDetail("message", message);
    return ex;
  }
}