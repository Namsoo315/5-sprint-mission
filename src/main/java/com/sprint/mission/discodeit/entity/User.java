package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class User implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final UUID id;
  private UUID profileId;    // binaryContent Id

  private String username;
  private String email;
  private String password;

  private final Instant createdAt;
  private Instant updatedAt;

  public User(String name, String email, String password, UUID profileId) {
    this.id = UUID.randomUUID();
    this.username = name;
    this.email = email;
    this.password = password;
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
    this.profileId = profileId;
  }

  public void update(String newUserName, String newEmail, String newPassword, UUID newProfileId) {
    boolean anyValueUpdated = false; // 업데이트 여부 확인

    if (newUserName != null && !newUserName.isEmpty()) {
      this.username = newUserName;
      anyValueUpdated = true;
    }

    if (newEmail != null && !newEmail.isEmpty()) {
      this.email = newEmail;
      anyValueUpdated = true;
    }

    if (newPassword != null && !newPassword.isEmpty()) {
      this.password = newPassword;
      anyValueUpdated = true;
    }

    if (newProfileId != null) {
      this.profileId = newProfileId;
      anyValueUpdated = true;
    }

    // 값이 하나라도 업데이트 됐으면 updatedAt 갱신
    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }


}
