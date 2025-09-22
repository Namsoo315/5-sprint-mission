package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class BinaryContentDeleteFailedException extends BinaryContentException {

  public BinaryContentDeleteFailedException() {
    super(ErrorCode.BINARY_CONTENT_DELETE_FAILED);
  }

  public static BinaryContentDeleteFailedException withMessage(String message) {
    BinaryContentDeleteFailedException ex = new BinaryContentDeleteFailedException();
    ex.addDetail("message", message);
    return ex;
  }
}