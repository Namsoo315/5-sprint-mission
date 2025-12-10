package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.event.S3UploadFailedEvent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentSaveFailedException;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentHandler {

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentService binaryContentService;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Retryable(
      maxAttempts = 5,
      backoff = @Backoff(delay = 1000),
      recover = "recover",
      retryFor = {BinaryContentSaveFailedException.class}
  )
  @Async(value = "taskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleAfterCommitCreate(BinaryContentCreatedEvent event) {
    try {
      binaryContentStorage.save(event.id(), event.bytes());
      binaryContentService.updateStatus(event.id(), BinaryContentStatus.SUCCESS);
      log.info("Success to save binary content file. id={}", event.id());
    } catch (Exception e) {
      log.error("Failed to save binary content file. id={}", event.id(), e);
      throw BinaryContentSaveFailedException.withMessage(e.getMessage());
    }

  }

  @Recover
  public void recover(BinaryContentSaveFailedException e, BinaryContentCreatedEvent event) {
    binaryContentService.updateStatus(event.id(), BinaryContentStatus.FAIL);

    S3UploadFailedEvent failedEvent = new S3UploadFailedEvent(
        event.id(),
        MDC.get("requestId"),
        e.getMessage()
    );

    applicationEventPublisher.publishEvent(failedEvent);
  }
}
