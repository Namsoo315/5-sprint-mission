package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Message implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final UUID authorId;
  private final UUID channelId;
  private final List<UUID> attachmentIds;    // binaryContent

  private String content;

  private final Instant createdAt;
  private Instant updatedAt;

  public Message(UUID authorId, UUID channelId, String content, List<UUID> attachmentIds) {
    this.id = UUID.randomUUID();
    this.authorId = authorId;
    this.channelId = channelId;
    this.content = content;
    this.attachmentIds = attachmentIds;
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
  }

  public void update(String newContent) {
    if (newContent != null && !newContent.isEmpty()) {
      this.content = newContent;
      this.updatedAt = Instant.now();
    }
  }
}
