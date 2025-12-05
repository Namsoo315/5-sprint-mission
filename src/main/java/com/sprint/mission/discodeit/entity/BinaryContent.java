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


@Table(name = "binary_contents")
@Entity
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(nullable = false)
  private Long size;

  @Column(name = "content_type", nullable = false, length = 100)
  private String contentType;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private BinaryContentStatus status;

  public void updateStatus(BinaryContentStatus status) {
    this.status = status;
  }
}
