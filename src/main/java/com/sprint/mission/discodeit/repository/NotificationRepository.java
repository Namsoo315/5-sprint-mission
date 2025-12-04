package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {


  @Query("SELECT n FROM Notification n JOIN FETCH n.user u WHERE u.id = :receiverId")
  List<Notification> findAllByUser_Id(@Param("receiverId") UUID receiverId);
}
