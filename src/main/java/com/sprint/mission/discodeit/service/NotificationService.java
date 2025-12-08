package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDTO;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

  List<NotificationDTO> findNotifications(UUID receiverId);

  void checkNotifications(UUID notificationId, UUID receiverId);
}
