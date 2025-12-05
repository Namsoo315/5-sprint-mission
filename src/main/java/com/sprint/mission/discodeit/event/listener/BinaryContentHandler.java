package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentSaveFailedException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  @Retryable(
      maxAttempts = 5,
      backoff = @Backoff(delay = 1000),
      recover = "recover"
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
    log.info("recover binary content file. id={}", event.id());

    binaryContentService.updateStatus(event.id(), BinaryContentStatus.FAIL);

    // 굉장히 싫다 쿼리 또 쓰기?
    User user = userRepository.findByUsername("admin").orElseThrow(
        UserNotFoundException::new);

    String requestId = MDC.get("requestId");
    Notification notification = Notification.builder()
        .user(user)// 어드민 한테 보내기
        .title("S3 파일 업로드 실패")
        .content(
            "RequestId: " + requestId + "\nBinaryContentId: " + event.id()
                + "\nError: " + e.getMessage())
        .build();

    notificationRepository.save(notification);
  }
}
