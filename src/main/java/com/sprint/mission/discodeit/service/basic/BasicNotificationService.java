package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDTO;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Override
  @Transactional(readOnly = true)
  public List<NotificationDTO> findNotifications(UUID receiverId) {
    List<Notification> notifications = notificationRepository.findAllByUser_Id(receiverId);

    return notificationMapper.toDto(notifications);
  }

  @Override
  @Transactional
  public void checkNotifications(UUID notificationId) {

    notificationRepository.deleteById(notificationId);
  }
}
