package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

public interface MessageService {

  Message createMessage(MessageCreateRequest messageCreateRequest,
      List<BinaryContentDTO> binaryContentDTO);

  Message findByMessageId(UUID messageId);

  List<Message> findAllByChannelId(UUID channelId);

  void updateMessage(UUID messageId, MessageUpdateRequest request);

  void deleteMessage(UUID messageId);

}
