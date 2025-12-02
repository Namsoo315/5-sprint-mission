package com.sprint.mission.discodeit.event.handler;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentSaveFailedException;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentHandler {

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentService binaryContentService;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleAfterCommitCreate(BinaryContentCreatedEvent event) {
    try {
      binaryContentStorage.save(event.id(), event.bytes());
      binaryContentService.updateStatus(event.id(), BinaryContentStatus.SUCCESS);
      log.info("Success to save binary content file. id={}", event.id());

    } catch (Exception e) {
      log.error("Failed to save binary content file. id={}", event.id(), e);
      binaryContentService.updateStatus(event.id(), BinaryContentStatus.FAIL);
    }

  }

}
