package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;


@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager em;

  public User createUser(String username, String email) {
    return User.builder()
        .username(username)
        .email(email)
        .password("test1234")
        .build();
  }

  @Test
  @DisplayName("username으로 사용자 조회 성공")
  void createUserTest_success() {
    // given
    User user = createUser("testUser", "testUser@gmail.com");

    // when
    User save = userRepository.save(user);

    // then
    assertThat(save).isNotNull();
  }

  @Test
  @DisplayName("username으로 사용자 조회 성공")
  void findByUsername_success() {
    // given
    User user = createUser("testUser", "testUser@gmail.com");
    userRepository.save(user);

    // when
    Optional<User> found = userRepository.findByUsername("testUser");

    // then
    assertThat(found).isPresent();
    assertThat(found.get().getUsername()).isEqualTo("testUser");
  }

  @Test
  @DisplayName("username 중복 저장 시 예외 발생")
  void saveUser_duplicateUsername_fail() {
    // given
    User user1 = createUser("testUser", "user1@gmail.com");
    User user2 = createUser("testUser", "user2@gmail.com");

    // when
    userRepository.save(user1);
    em.flush(); // DB에 반영

    // then
    assertThatThrownBy(() -> {
      userRepository.save(user2);
      em.flush(); // 중복 username으로 인해 예외 발생
    }).isInstanceOf(org.hibernate.exception.ConstraintViolationException.class);
  }


  @Test
  @DisplayName("Email로 사용자 조회 성공")
  void findByEmail_success() {
    // given
    User user = createUser("testUser", "testUser@gmail.com");
    userRepository.save(user);

    // when
    Optional<User> found = userRepository.findByEmail("testUser@gmail.com");

    // then
    assertThat(found).isPresent();
    assertThat(found.get().getEmail()).isEqualTo("testUser@gmail.com");
  }

  @Test
  @DisplayName("Email로 사용자 조회 실패")
  void findByEmail_fail() {
    // given
    User user1 = createUser("testUser1", "user@gmail.com");
    User user2 = createUser("testUser2", "user@gmail.com");

    // when
    userRepository.save(user1);
    em.flush(); // DB에 반영

    // then
    assertThatThrownBy(() -> {
      userRepository.save(user2);
      em.flush(); // 중복 username으로 인해 예외 발생
    }).isInstanceOf(org.hibernate.exception.ConstraintViolationException.class);
  }

}