package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "read_statuses")
@Getter
@SuperBuilder
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "user_id", unique = true, nullable = false)
  private User user;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "channel_id", unique = true, nullable = false)
  private Channel channel;

  @Column(name = "last_read_at", nullable = false)
  private Instant lastReadAt;

//  public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
//    this.userId = userId;
//    this.channelId = channelId;
//    this.lastReadAt = lastReadAt;
//  }
//
//  public void update(Instant newLastReadAt) {
//    boolean anyValueUpdated = false;
//    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
//      this.lastReadAt = newLastReadAt;
//      anyValueUpdated = true;
//    }
//
//    if (anyValueUpdated) {
//      // updateat 추가
//    }
//  }
}
