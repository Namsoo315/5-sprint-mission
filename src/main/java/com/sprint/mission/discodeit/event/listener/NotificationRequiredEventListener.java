package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationRequiredEventListener {

  private final NotificationRepository notificationRepository;

  @Caching(evict = {
      @CacheEvict(value = "notifications", key = "#event.userId()")
  })
  @Async(value = "taskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void on(MessageCreatedEvent event) {
    event.enabledUsers().stream()
        // 2. 메시지 보낸 사람 제외
        .filter(readStatus -> !readStatus.getUser().getId().equals(event.userId()))
        .forEach(readStatus -> {

          Notification notification = Notification.builder().user(readStatus.getUser())
              .title("보낸 사람 (#" + readStatus.getUser().getUsername() + ")").content(event.content())
              .build();

          notificationRepository.save(notification);
        });
  }

  @Caching(evict = {
      @CacheEvict(value = "notifications", key = "#event.user().id")
  })
  @Async(value = "taskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void on(RoleUpdatedEvent event) {

    Notification notification = Notification.builder().user(event.user()).title("권한이 변경되었습니다.")
        .content(event.oldRole() + " -> " + event.newRole()).build();

    notificationRepository.save(notification);
  }
}
