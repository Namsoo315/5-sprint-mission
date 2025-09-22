package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

  @InjectMocks
  private BasicChannelService channelService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ChannelMapper channelMapper;

  private Channel publicChannel;
  private Channel privateChannel;
  private ChannelDTO publicDTO;
  private ChannelDTO privateDTO;

  @BeforeEach
  void setUp() {
    publicChannel = Channel.builder()
        .id(UUID.randomUUID())
        .type(ChannelType.PUBLIC)
        .name("publicChannel")
        .description("Public Desc")
        .build();

    privateChannel = Channel.builder()
        .id(UUID.randomUUID())
        .type(ChannelType.PRIVATE)
        .build();

    publicDTO = new ChannelDTO(publicChannel.getId(), publicChannel.getType(),
        publicChannel.getName(),
        publicChannel.getDescription(), null, null);

    privateDTO = new ChannelDTO(privateChannel.getId(), privateChannel.getType(),
        null, null, null, null);
  }

  @Test
  @DisplayName("Public 채널 생성 성공 ✅")
  void createPublicChannel_success() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("publicChannel",
        "Public Desc");

    given(channelRepository.save(any(Channel.class))).willReturn(publicChannel);
    given(channelMapper.toDto(publicChannel)).willReturn(publicDTO);

    ChannelDTO result = channelService.createPublicChannel(request);

    assertThat(result).isEqualTo(publicDTO);
    then(channelRepository).should(times(1)).save(any(Channel.class));
    then(channelMapper).should(times(1)).toDto(any(Channel.class));
  }

  @Test
  @DisplayName("Private 채널 생성 성공 ✅")
  void createPrivateChannel_success() {
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();

    PrivateChannelCreateRequest request =
        new PrivateChannelCreateRequest(List.of(userId1, userId2));

    given(channelRepository.save(any(Channel.class))).willReturn(privateChannel);
    given(userRepository.findById(userId1)).willReturn(Optional.of(mock(User.class)));
    given(userRepository.findById(userId2)).willReturn(Optional.of(mock(User.class)));
    given(channelMapper.toDto(privateChannel)).willReturn(privateDTO);

    ChannelDTO result = channelService.createPrivateChannel(request);

    assertThat(result).isEqualTo(privateDTO);
    then(channelRepository).should(times(1)).save(any(Channel.class));
    then(readStatusRepository).should(times(2)).save(any(ReadStatus.class));
    then(channelMapper).should(times(1)).toDto(any(Channel.class));
  }

  @Test
  @DisplayName("Public 채널 정보 수정 성공 ✅")
  void updateChannel_success() {
    UUID channelId = publicChannel.getId();
    Channel updateTarget = Channel.builder()
        .id(channelId)
        .type(ChannelType.PUBLIC)
        .name("oldName")
        .description("oldDesc")
        .build();

    ChannelUpdateRequest request = new ChannelUpdateRequest("publicChannel", "Public Desc");

    given(channelRepository.findById(channelId)).willReturn(Optional.of(updateTarget));
    given(channelRepository.save(any(Channel.class))).willReturn(publicChannel);
    given(channelMapper.toDto(publicChannel)).willReturn(publicDTO);

    ChannelDTO result = channelService.updateChannel(channelId, request);

    assertThat(result).isEqualTo(publicDTO);
    then(channelRepository).should(times(1)).findById(channelId);
    then(channelRepository).should(times(1)).save(any(Channel.class));
    then(channelMapper).should(times(1)).toDto(any(Channel.class));
  }

  @Test
  @DisplayName("Public 채널 정보 수정 실패 ❌ - PRIVATE 채널")
  void updateChannel_fail_privateChannel() {
    UUID channelId = privateChannel.getId();
    ChannelUpdateRequest request = new ChannelUpdateRequest("privateChannel", "Private Desc");

    given(channelRepository.findById(channelId)).willReturn(Optional.of(privateChannel));

    assertThatThrownBy(() -> channelService.updateChannel(channelId, request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("PRIVATE 채널은 수정할 수 없습니다.");

    then(channelRepository).should(times(1)).findById(channelId);
    then(channelRepository).should(never()).save(any(Channel.class));
  }

  @Test
  @DisplayName("채널 삭제 성공 ✅")
  void deleteChannel_success() {
    UUID channelId = publicChannel.getId();
    given(channelRepository.existsById(channelId)).willReturn(true);

    channelService.deleteChannel(channelId);

    then(channelRepository).should(times(1)).deleteById(channelId);
    then(messageRepository).should(times(1)).deleteByChannelId(channelId);
    then(readStatusRepository).should(times(1)).deleteByChannelId(channelId);
  }

  @Test
  @DisplayName("채널 삭제 실패 ❌ - 존재하지 않는 채널")
  void deleteChannel_fail_notFound() {
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(false);

    assertThatThrownBy(() -> channelService.deleteChannel(channelId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("존재하지 않는 채널 입니다");

    then(channelRepository).should(times(1)).existsById(channelId);
    then(channelRepository).should(never()).deleteById(any(UUID.class));
    then(messageRepository).should(never()).deleteByChannelId(any(UUID.class));
    then(readStatusRepository).should(never()).deleteByChannelId(any(UUID.class));
  }
}
