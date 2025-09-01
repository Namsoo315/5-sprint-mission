package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  @Transactional
  public ReadStatus createReadStatus(ReadStatusCreateRequest request) {

    // 1. 호환성 체크 User, Channel 체크
    User user = userRepository.findById(request.userId()).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 회원입니다."));

    Channel channel = channelRepository.findById(request.channelId()).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 채널입니다."));

    // 1-2. 만약 channelId와 userId가 매치되는 readStatus가 존재하면 예외 처리
    Optional<ReadStatus> existing = readStatusRepository.findByUserIdAndChannelId(user.getId(),
        channel.getId());

    if (existing.isPresent()) {
      throw new IllegalArgumentException("이미 같은 channelId와 UserId가 존재하는 상태정보가 있습니다.");
    }

    // 2. 생성
    ReadStatus readStatus = ReadStatus.builder()
        .user(user)
        .channel(channel)
        .lastReadAt(request.lastReadAt())
        .build();
    readStatusRepository.save(readStatus);

    return readStatus;
  }

  @Override
  @Transactional
  public ReadStatus findByReadStatusId(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 상태정보입니다."));
  }

  @Override
  @Transactional
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId);
  }

  @Override
  @Transactional
  public ReadStatus updateReadStatus(UUID readStatusId, ReadStatusUpdateRequest request) {
    Instant newLastReadAt = request.newLastReadAt();
    // 1. 호환성 체크
    ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 상태정보입니다."));

    // 2. 상태정보 업데이트 후 Repository save(update)
    readStatusRepository.save(readStatus);

    return readStatus;
  }

  @Override
  @Transactional
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new NoSuchElementException("존재하지 않는 상태정보입니다.");
    }
    readStatusRepository.deleteById(readStatusId);
  }
}
