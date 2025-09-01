package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user_statuses")
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt;


  @Transient
  public boolean isOnline() {
    Instant now = Instant.now();
    // 규칙 예시: 최근 5분 내 활동이면 온라인
    return lastActiveAt != null && lastActiveAt.isAfter(now.minusSeconds(300));
  }
}
