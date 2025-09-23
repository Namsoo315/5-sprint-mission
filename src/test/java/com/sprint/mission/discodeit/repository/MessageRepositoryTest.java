package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private TestEntityManager em;

  private User testUser1;
  private User testUser2;
  private Channel testChannel;

  @BeforeEach
  void setUp() {
    // 1. 테스트용 User 생성
    testUser1 = User.builder()
        .username("testUser1")
        .email("testUser1@example.com")
        .password("password")
        .build();
    em.persist(testUser1);

    testUser2 = User.builder()
        .username("testUser2")
        .email("testUser2@example.com")
        .password("password")
        .build();
    em.persist(testUser2);

    // 2. 테스트용 Channel 생성
    testChannel = Channel.builder()
        .type(ChannelType.PUBLIC)
        .name("testChannel")
        .description("testChannel")
        .build();
    em.persist(testChannel);

    em.flush();
    em.clear();
  }

  private Message createMessage(User user, String content, Instant createdAt) {
    return Message.builder()
        .author(user)
        .channel(testChannel)
        .content(content)
        .createdAt(createdAt)
        .build();
  }

  @Test
  @DisplayName("채널 ID로 메시지 조회 성공 (페이징)")
  void findAllByChannelIdWithPageable_success() {
    // given
    Message message1 = createMessage(testUser1, "테스트 1이 보낸 메시지", Instant.now());
    Message message2 = createMessage(testUser2, "테스트 2가 보낸 메시지", Instant.now());

    messageRepository.save(message1);
    messageRepository.save(message2);
    em.flush();
    em.clear();

    // when
    Slice<Message> slice = messageRepository.findAllByChannelIdWithPageable(testChannel.getId(),
        Pageable.ofSize(10));

    // then
    assertThat(slice.getContent()).hasSize(2);
    assertThat(slice.getContent())
        .extracting(message -> message.getAuthor().getUsername())
        .containsExactlyInAnyOrder("testUser1", "testUser2");
  }

  @Test
  @DisplayName("채널 ID로 메시지 조회 실패 (존재하지 않는 채널)")
  void findAllByChannelIdWithPageable_fail() {
    Slice<Message> slice = messageRepository.findAllByChannelIdWithPageable(UUID.randomUUID(),
        Pageable.ofSize(10));
    assertThat(slice.getContent()).isEmpty();
  }

  @Test
  @DisplayName("채널 ID와 cursor 기준 메시지 조회 성공")
  void findAllByChannelIdWithAuthorAndAttachments_success() {
    // given
    Instant cursor = Instant.now();
    Message oldMsg = createMessage(testUser1, "이전 메시지", cursor.minusSeconds(10));
    Message newMsg = createMessage(testUser2, "최신 메시지", cursor.plusSeconds(10));

    messageRepository.save(oldMsg);
    messageRepository.save(newMsg);
    em.flush();
    em.clear();

    // when
    Slice<Message> slice = messageRepository.findAllByChannelIdWithAuthorAndAttachments(
        testChannel.getId(), cursor, Pageable.ofSize(10));

    // then
    assertThat(slice.getContent()).hasSize(2);
    assertThat(slice.getContent().get(0).getAuthor().getUsername()).isEqualTo("testUser1");
  }

  @Test
  @DisplayName("채널 ID와 cursor 기준 메시지 조회 실패 (조건 불만족)")
  void findAllByChannelIdWithAuthorAndAttachments_fail() {
    Slice<Message> slice = messageRepository.findAllByChannelIdWithAuthorAndAttachments(
        UUID.randomUUID(), Instant.now(), Pageable.ofSize(10));
    assertThat(slice.getContent()).isEmpty();
  }

  @Test
  @DisplayName("채널 메시지 최신 메시지 조회 성공")
  void findTopByChannelIdOrderByCreatedAtDesc_success() {
    // given
    Instant now = Instant.now();
    messageRepository.save(createMessage(testUser1, "메시지1", now.minusSeconds(10)));
    Message message2 = messageRepository.save(createMessage(testUser2, "메시지2", now));

    em.flush();
    em.clear();

    // when

    // then
    assertThat(messageRepository.findTopByChannelIdOrderByCreatedAtDesc(testChannel.getId()))
        .isPresent()
        .get()
        .satisfies(instant -> assertThat(instant.truncatedTo(ChronoUnit.MICROS))
            .isEqualTo(message2.getCreatedAt().truncatedTo(ChronoUnit.MICROS)));
  }

  @Test
  @DisplayName("채널 메시지 삭제 성공")
  void deleteByChannelId_success() {

    // given
    messageRepository.save(createMessage(testUser1, "메시지1", Instant.now()));
    messageRepository.save(createMessage(testUser2, "메시지2", Instant.now()));

    em.flush();

    messageRepository.deleteByChannelId(testChannel.getId());
    em.flush();
    em.clear();

    // when

    // then
    Slice<Message> slice = messageRepository.findAllByChannelIdWithPageable(testChannel.getId(),
        Pageable.ofSize(10));
    assertThat(slice.getContent()).isEmpty();
  }

  @Test
  @DisplayName("채널 메시지 삭제 실패 (존재하지 않는 채널)")
  void deleteByChannelId_fail() {
    // given

    // when
    messageRepository.deleteByChannelId(UUID.randomUUID());

    // then
    Slice<Message> slice = messageRepository.findAllByChannelIdWithPageable(testChannel.getId(),
        Pageable.ofSize(10));
    assertThat(slice.getContent()).isEmpty();
  }
}