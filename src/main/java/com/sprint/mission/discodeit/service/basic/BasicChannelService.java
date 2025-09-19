package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final ChannelMapper channelMapper;

  @Override
  @Transactional
  public ChannelDTO createPublicChannel(PublicChannelCreateRequest request) {
    // Public은 생성시 기존 로직을 유지.
    Channel channel = Channel.builder()
        .type(ChannelType.PUBLIC)
        .name(request.name())
        .description(request.description())
        .build();
    channelRepository.save(channel);

    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public ChannelDTO createPrivateChannel(PrivateChannelCreateRequest request) {
    // Private 채널 생성
    Channel channel = Channel.builder()
        .type(ChannelType.PRIVATE)
        .build();
    channelRepository.save(channel);

    // readStatus 저장하기
    request.participantIds().stream()
        .map(userId -> userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다.")))
        .map(user -> ReadStatus.builder()
            .user(user)
            .channel(channel)
            .lastReadAt(Instant.now())
            .build())
        .forEach(readStatusRepository::save);

    return channelMapper.toDto(channel);
  }


  @Override
  @Transactional(readOnly = true)
  public ChannelDTO findByChannelId(UUID channelId) {
    // 1. 호환성 확인
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 채널입니다."));

    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDTO> findAllByUserId(UUID userId) {
    // 호환성 체크
    userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

    // 1. Private 채널은 User가 참여한 채널만 조회하기 위한 ChannelId를 List화 시킴.
    List<Channel> subscribeChannels = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC
            || (channel.getType() == ChannelType.PRIVATE && subscribeChannels.contains(channel)))
        .map(channelMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public ChannelDTO updateChannel(UUID channelId, ChannelUpdateRequest request) {
    // 1. 호환성 체크
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 채널입니다."));

    // 1-2. PRIVATE일 경우 update 불가능
    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
    }
    channelRepository.save(channel);
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public void deleteChannel(UUID channelId) {

    // 관련 도메인 같이 삭제 Channel, Message, ReadStatus
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("존재하지 않는 채널 입니다");
    }
    channelRepository.deleteById(channelId);

    messageRepository.deleteByChannelId(channelId);

    readStatusRepository.deleteByChannelId(channelId);
  }
}
