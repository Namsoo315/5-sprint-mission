package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Table(name = "channels")
@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseEntity {

  @Column(length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "channel_type", nullable = false)
  private ChannelType type;
}
