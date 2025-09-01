package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "users")
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {


  @Column(unique = true, nullable = false, length = 50)
  private String username;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  @Column(nullable = false, length = 60)
  private String password;


  @OneToOne(orphanRemoval = true)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @OneToOne
  private UserStatus status;

//  public void update(String newUserName, String newEmail, String newPassword, UUID newProfileId) {
//    boolean anyValueUpdated = false; // 업데이트 여부 확인
//
//    if (newUserName != null && !newUserName.isEmpty()) {
//      this.username = newUserName;
//      anyValueUpdated = true;
//    }
//
//    if (newEmail != null && !newEmail.isEmpty()) {
//      this.email = newEmail;
//      anyValueUpdated = true;
//    }
//
//    if (newPassword != null && !newPassword.isEmpty()) {
//      this.password = newPassword;
//      anyValueUpdated = true;
//    }
//
//    if (newProfileId != null) {
//      this.profileId = newProfileId;
//      anyValueUpdated = true;
//    }
//
//    if (anyValueUpdated) {
//      // 값이 하나라도 업데이트 됐으면 updatedAt 갱신
//    }
//  }
//

}
