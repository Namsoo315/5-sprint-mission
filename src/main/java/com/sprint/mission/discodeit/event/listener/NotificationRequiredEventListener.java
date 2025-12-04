package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationRequiredEventListener {

  private final ReadStatusRepository readStatusRepository;
  private final NotificationRepository notificationRepository;

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void on(MessageCreatedEvent event) {

    // 1. 채널에서 알림이 활성화된 ReadStatus 조회
    List<ReadStatus> enabledUsers = readStatusRepository
        .findAllByChannelIdAndNotificationEnabledTrue(event.channelId());

    enabledUsers.stream()
        // 2. 메시지 보낸 사람 제외
        .filter(readStatus -> !readStatus.getUser().getId().equals(event.userId()))
        .forEach(readStatus -> {

          Notification notification = Notification.builder()
              .user(readStatus.getUser())
              .title("보낸 사람 (#" + readStatus.getUser().getUsername() + ")")
              .content(event.content())
              .build();

          notificationRepository.save(notification);
        });
  }


  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void on(RoleUpdatedEvent event) {

    Notification notification = Notification.builder()
        .user(event.user())
        .title("권한이 변경되었습니다.")
        .content(event.oldRole() + " -> " + event.newRole())
        .build();

    notificationRepository.save(notification);
  }
}
