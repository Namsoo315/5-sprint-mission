package com.sprint.mission.discodeit.mapper.helper;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelHelper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  @Named("getParticipants")
  public List<UserDTO> getParticipants(Channel channel) {
    return readStatusRepository.findAllByChannelId(channel.getId()).stream()
        .map(ReadStatus::getUser)
        .map(userMapper::toDto)
        .collect(Collectors.toList());
  }

  @Named("getLastMessageAt")
  public Instant getLastMessageAt(Channel channel) {
    return messageRepository.findLastMessageAtByChannelId(channel.getId())
        .map(Message::getCreatedAt)
        .orElse(null);
  }
}
