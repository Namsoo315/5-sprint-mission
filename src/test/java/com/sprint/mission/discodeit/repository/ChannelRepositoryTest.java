package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  private Channel testChannel;

  @BeforeEach
  void setUp() {
    channelRepository.deleteAll();
    
    testChannel = Channel.builder()
        .name("testChannel")
        .description("테스트 채널")
        .type(ChannelType.PUBLIC)
        .build();
    channelRepository.save(testChannel);
  }

  private Channel createChannel() {
    return Channel.builder()
        .name("newChannel")
        .description("새 채널")
        .type(ChannelType.PUBLIC)
        .build();
  }

  @Test
  @DisplayName("채널 저장 성공")
  void saveChannel_success() {
    Channel channel = createChannel();

    Channel saved = channelRepository.save(channel);
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getName()).isEqualTo("newChannel");
  }


  @Test
  @DisplayName("채널 조회 성공")
  void findById_success() {
    Channel found = channelRepository.findById(testChannel.getId()).orElseThrow();
    assertThat(found.getName()).isEqualTo("testChannel");
  }

  @Test
  @DisplayName("채널 조회 실패 (존재하지 않는 ID)")
  void findById_fail() {
    assertThat(channelRepository.findById(UUID.randomUUID())).isEmpty();
  }

  @Test
  @DisplayName("채널 삭제 성공")
  void deleteChannel_success() {
    channelRepository.delete(testChannel);
    assertThat(channelRepository.findById(testChannel.getId())).isEmpty();
  }

  @Test
  @DisplayName("채널 삭제 실패 (존재하지 않는 채널)")
  void deleteChannel_fail() {
    UUID nonExistId = UUID.randomUUID();
    // deleteById는 존재하지 않아도 예외 발생하지 않음
    channelRepository.deleteById(nonExistId);
    assertThat(channelRepository.findById(nonExistId)).isEmpty();
  }
}
