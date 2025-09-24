package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.willDoNothing;

import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.InvalidMessageParameterException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  @InjectMocks
  private BasicMessageService messageService;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private MessageMapper messageMapper;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private PageResponseMapper pageResponseMapper;

  @Mock
  private Pageable pageable;

  private User user;
  private Channel channel;
  private Message message;
  private MessageDTO messageDTO;

  @BeforeEach
  void setUp() {
    binaryContentRepository.deleteAll();
    channelRepository.deleteAll();
    messageRepository.deleteAll();
    userRepository.deleteAll();

    user = User.builder().id(UUID.randomUUID()).username("testUser").build();
    UserDTO userDTO = new UserDTO(user.getId(), "testUser", "test@example.com", null, true);

    channel = Channel.builder().id(UUID.randomUUID()).name("testChannel").build();

    message = Message.builder()
        .id(UUID.randomUUID())
        .author(user)
        .channel(channel)
        .content("Hello World")
        .build();

    messageDTO = new MessageDTO(
        message.getId(),
        Instant.now(),
        Instant.now(),
        message.getContent(),
        channel.getId(),
        userDTO,
        List.of() // attachments mock
    );
  }

  @Test
  @DisplayName("메시지 생성 성공")
  void createMessage_success() {
    MessageCreateRequest request = new MessageCreateRequest(user.getId(), channel.getId(),
        "Hello World");

    given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
    given(channelRepository.findById(channel.getId())).willReturn(Optional.of(channel));
    given(messageRepository.save(any(Message.class))).willReturn(message);
    given(messageMapper.toDto(message)).willReturn(messageDTO);

    MessageDTO result = messageService.createMessage(request, null);

    assertThat(result).isEqualTo(messageDTO);
    then(messageRepository).should(times(1)).save(any(Message.class));
    then(messageMapper).should(times(1)).toDto(any(Message.class));
  }

  @Test
  @DisplayName("메시지 생성 실패 (존재하지 않는 회원)")
  void createMessage_fail_userNotFound() {
    MessageCreateRequest request = new MessageCreateRequest(UUID.randomUUID(), channel.getId(),
        "Hello");

    given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.createMessage(request, null))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining("사용자를 찾을 수 없습니다.");

    then(messageRepository).should(never()).save(any(Message.class));
  }

  @Test
  @DisplayName("메시지 생성 실패 (존재하지 않는 채널)")
  void createMessage_fail_channelNotFound() {
    MessageCreateRequest request = new MessageCreateRequest(user.getId(), UUID.randomUUID(),
        "Hello");

    given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
    given(channelRepository.findById(any(UUID.class))).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.createMessage(request, null))
        .isInstanceOf(ChannelNotFoundException.class)
        .hasMessageContaining("채널을 찾을 수 없습니다.");

    then(messageRepository).should(never()).save(any(Message.class));
  }

  @Test
  @DisplayName("메시지 생성 실패 (mapper 예외)")
  void createMessage_fail_mapperException() {
    MessageCreateRequest request = new MessageCreateRequest(user.getId(), channel.getId(), "Hello");

    given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
    given(channelRepository.findById(channel.getId())).willReturn(Optional.of(channel));
    given(messageRepository.save(any(Message.class))).willReturn(message);
    given(messageMapper.toDto(message)).willThrow(new RuntimeException("Mapper Error"));

    assertThatThrownBy(() -> messageService.createMessage(request, null))
        .isInstanceOf(InvalidMessageParameterException.class)
        .hasMessageContaining("잘못된 메시지 파라미터입니다.");
  }

  @Test
  @DisplayName("메시지 수정 성공")
  void updateMessage_success() {
    MessageUpdateRequest request = new MessageUpdateRequest("Updated Content");

    given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));
    given(messageRepository.save(any(Message.class))).willReturn(message);
    given(messageMapper.toDto(message)).willReturn(messageDTO);

    MessageDTO result = messageService.updateMessage(message.getId(), request);

    assertThat(result).isEqualTo(messageDTO);
    then(messageRepository).should(times(1)).save(any(Message.class));
  }

  @Test
  @DisplayName("메시지 수정 실패 (존재하지 않는 메시지)")
  void updateMessage_fail_messageNotFound() {
    MessageUpdateRequest request = new MessageUpdateRequest("Updated Content");

    given(messageRepository.findById(any(UUID.class))).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.updateMessage(UUID.randomUUID(), request))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("존재하지 않는 메시지입니다.");

    then(messageRepository).should(never()).save(any(Message.class));
  }

  @Test
  @DisplayName("메시지 삭제 성공")
  void deleteMessage_success() {
    given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));
    willDoNothing().given(binaryContentRepository).deleteAll(message.getAttachments());

    messageService.deleteMessage(message.getId());

    then(messageRepository).should(times(1)).deleteById(message.getId());
  }

  @Test
  @DisplayName("채널 메시지 페이징 조회 성공")
  void findAllByChannelId_success() {
    List<Message> messages = List.of(message);
    PageImpl<Message> slice = new PageImpl<>(messages);

    given(channelRepository.findById(channel.getId())).willReturn(Optional.of(channel));
    given(messageRepository.findAllByChannelIdWithPageable(channel.getId(), pageable)).willReturn(
        slice);
    given(messageMapper.toDto(message)).willReturn(messageDTO);
    given(pageResponseMapper.fromSlice(slice, List.of(messageDTO))).willReturn(
        mock(PageResponse.class));

    PageResponse<MessageDTO> result = messageService.findAllByChannelId(channel.getId(), null,
        pageable);

    assertThat(result).isNotNull();
    then(messageRepository).should(times(1))
        .findAllByChannelIdWithPageable(channel.getId(), pageable);
  }

  @Test
  @DisplayName("채널 메시지 조회 실패 (존재하지 않는 채널)")
  void findAllByChannelId_fail_channelNotFound() {
    given(channelRepository.findById(any(UUID.class))).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.findAllByChannelId(UUID.randomUUID(), null, pageable))
        .isInstanceOf(ChannelNotFoundException.class)
        .hasMessageContaining("채널을 찾을 수 없습니다.");

    then(messageRepository).should(never()).findAllByChannelIdWithPageable(any(), any());
  }
}
