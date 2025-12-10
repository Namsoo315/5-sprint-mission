package com.sprint.mission.discodeit.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.event.S3UploadFailedEvent;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRequiredTopicListener {

  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final NotificationRepository notificationRepository;
  private final ObjectMapper objectMapper;

  @Transactional
  @KafkaListener(topics = "discodeit.MessageCreatedEvent")
  public void onMessageCreatedEvent(String kafkaEvent) {
    try {
      MessageCreatedEvent event = objectMapper.readValue(kafkaEvent, MessageCreatedEvent.class);

      // 채널에서 알림이 활성화된 ReadStatus 조회 (Listener쪽 안에 있으니 Transactional BeforeCommit이 먹히지 않음.)
      List<ReadStatus> enabledUsers = readStatusRepository.findAllByChannelIdAndNotificationEnabledTrue(
          event.channelId());

      enabledUsers.stream()
          // 2. 메시지 보낸 사람 제외
          .filter(readStatus -> !readStatus.getUser().getId().equals(event.userId()))
          .forEach(readStatus -> {

            Notification notification = Notification.builder()
                .receiverId(readStatus.getUser().getId())
                .title("보낸 사람 (#" + readStatus.getUser().getUsername() + ")")
                .content(event.content())
                .build();

            notificationRepository.save(notification);
          });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Transactional
  @KafkaListener(topics = "discodeit.RoleUpdatedEvent")
  public void onRoleUpdatedEvent(String kafkaEvent) {
    try {
      RoleUpdatedEvent event = objectMapper.readValue(kafkaEvent, RoleUpdatedEvent.class);
      Notification notification = Notification.builder()
          .receiverId(event.userId())
          .title("권한이 변경되었습니다.")
          .content(event.oldRole() + " -> " + event.newRole())
          .build();

      notificationRepository.save(notification);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Transactional
  @KafkaListener(topics = "discodeit.S3UploadFailedEvent")
  public void onS3UploadFailedEvent(String kafkaEvent) {
    try {
      S3UploadFailedEvent event = objectMapper.readValue(kafkaEvent, S3UploadFailedEvent.class);

      User user = userRepository.findByUsername("admin").orElseThrow(UserNotFoundException::new);

      Notification notification = Notification.builder()
          .receiverId(user.getId())
          .title("S3 업로드 실패")
          .content(
              "RequestId: " + event.requestId() + "\n" +
                  "BinaryContentId: " + event.binaryContentId() + "\n" +
                  "Error: " + event.errorMessage()
          )
          .build();

      notificationRepository.save(notification);

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
