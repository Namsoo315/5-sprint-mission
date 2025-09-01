package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "messages")
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {

  @Column
  private String content;

  @ManyToOne
  @JoinColumn(name = "author_id") // nullable = true 필수
  private User author;


  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @OneToMany(cascade = CascadeType.REMOVE)
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachments;

//  public Message(UUID authorId, UUID channelId, String content, List<UUID> attachmentIds) {
//    this.authorId = authorId;
//    this.channelId = channelId;
//    this.content = content;
//    this.attachmentIds = attachmentIds;
//  }
//
//  public void update(String newContent) {
//    if (newContent != null && !newContent.isEmpty()) {
//      this.content = newContent;
//      // update 시간 추가.
//    }
//  }
}
