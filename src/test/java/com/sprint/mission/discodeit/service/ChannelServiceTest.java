package com.sprint.mission.discodeit.service;

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
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.util.List;
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
class ChannelServiceTest {

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
    publicChannel = Channel.builder().id(UUID.randomUUID()).type(ChannelType.PUBLIC)
        .name("publicChannel").description("Public Desc").build();

    privateChannel = Channel.builder().id(UUID.randomUUID()).type(ChannelType.PRIVATE).build();

    publicDTO = new ChannelDTO(publicChannel.getId(), publicChannel.getType(),
        publicChannel.getName(), publicChannel.getDescription(), null, null);

    privateDTO = new ChannelDTO(privateChannel.getId(), privateChannel.getType(), null, null, null,
        null);
  }

  @Test
  @DisplayName("Public 채널 생성 성공")
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
  @DisplayName("Private 채널 생성 성공")
  void createPrivateChannel_success() {
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();

    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
        List.of(userId1, userId2));

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
  @DisplayName("Public 채널 정보 수정 성공")
  void updateChannel_success() {
    UUID channelId = publicChannel.getId();
    Channel updateTarget = Channel.builder().id(channelId).type(ChannelType.PUBLIC).name("oldName")
        .description("oldDesc").build();

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
  @DisplayName("존재하지 않는 채널 업데이트 실패")
  void updateChannel_fail_notFound() {
    // given
    UUID notExistsChannelId = UUID.randomUUID();
    ChannelUpdateRequest request = new ChannelUpdateRequest("newName", "newDesc");

    given(channelRepository.findById(notExistsChannelId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(
        () -> channelService.updateChannel(notExistsChannelId, request)).isInstanceOf(
        ChannelNotFoundException.class);

    // save는 호출되지 않아야 함
    then(channelRepository).should(never()).save(any(Channel.class));
  }

  @Test
  @DisplayName("채널 삭제 성공")
  void deleteChannel_success() {
    UUID channelId = publicChannel.getId();
    given(channelRepository.existsById(channelId)).willReturn(true);

    channelService.deleteChannel(channelId);

    then(channelRepository).should(times(1)).deleteById(channelId);
    then(messageRepository).should(times(1)).deleteByChannelId(channelId);
    then(readStatusRepository).should(times(1)).deleteByChannelId(channelId);
  }
}
