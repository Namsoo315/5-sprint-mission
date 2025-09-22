package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

  @Query("SELECT us FROM UserStatus us JOIN FETCH us.user u WHERE us.user.id = :userId")
  Optional<UserStatus> findByUserId(@Param("userId") UUID userId);

  void deleteByUserId(UUID userId);

}
