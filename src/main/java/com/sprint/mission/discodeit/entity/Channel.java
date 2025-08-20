package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Channel implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final UUID id;

  private String name;
  private String description;
  private final ChannelType type;

  private final Instant createdAt;
  private Instant updatedAt;

  public Channel(ChannelType type, String name, String description) {
    this.id = UUID.randomUUID();
    this.type = type;
    this.name = name;
    this.description = description;
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
  }

  public void update(String newName, String newDescription) {
    boolean anyValueUpdated = false;

    if (newName != null && !newName.isEmpty()) {
      this.name = newName;
      anyValueUpdated = true;
    }

    if (newDescription != null && !newDescription.isEmpty()) {
      this.description = newDescription;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }

}
