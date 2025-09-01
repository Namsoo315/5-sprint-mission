package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

//  public void updateStatus(Instant lastActivateAt) {
//    boolean anyValueUpdated = false;
//    if (lastActivateAt != null && !lastActivateAt.equals(this.lastActiveAt)) {
//      this.lastActiveAt = lastActivateAt;
//      anyValueUpdated = true;
//    }
//
//    if (anyValueUpdated) {
//      // 값이 하나라도 업데이트 됐으면 updatedAt 갱신
//    }
//
//  }
//
//  // 현재 시간 기준 5분 이내에 업데이트된 경우 온라인으로 간주;
//  public boolean isOnline() {
//    Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
//
//    return lastActiveAt.isAfter(instantFiveMinutesAgo);
//  }
}
