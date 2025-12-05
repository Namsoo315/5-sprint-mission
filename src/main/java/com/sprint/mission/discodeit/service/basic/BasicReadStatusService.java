package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusSaveFailedException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @Transactional
  public ReadStatusDTO createReadStatus(ReadStatusCreateRequest request) {

    // 1. 호환성 체크 User, Channel 체크
    User user = userRepository.findById(request.userId()).orElseThrow(UserNotFoundException::new);

    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(ChannelNotFoundException::new);

    // 2.이미 존재하면 새로 생성하지 않고 반환
    ReadStatus save = null;
    try {
      save = readStatusRepository.findByUserIdAndChannelId(user.getId(), channel.getId())
          .orElseGet(() -> {
            ReadStatus rs = ReadStatus.builder().user(user).channel(channel)
                .lastReadAt(Instant.now())
                .notificationEnabled(false)
                .build();
            return readStatusRepository.save(rs);
          });
    } catch (Exception e) {
      throw ReadStatusSaveFailedException.withMessage(e.getMessage());
    }
    return readStatusMapper.toDto(save);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusDTO> findAllByUserId(UUID userId) {
    List<ReadStatus> saves = readStatusRepository.findAllByUserId(userId);
    return readStatusMapper.toDto(saves);
  }

  @Override
  @Transactional
  public ReadStatusDTO updateReadStatus(UUID readStatusId, ReadStatusUpdateRequest request) {
    // 1. 호환성 체크
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(ReadStatusNotFoundException::new);

    readStatus.updateReadStatus(request);

    // 2. 상태정보 업데이트 후 Repository save(update)
    ReadStatus save = readStatusRepository.save(readStatus);

    return readStatusMapper.toDto(save);
  }

  @Override
  @Transactional
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new ReadStatusNotFoundException();
    }
    readStatusRepository.deleteById(readStatusId);
  }
}
