package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  Message createMessage(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments);

  Message findByMessageId(UUID messageId);

  List<Message> findAllByChannelId(UUID channelId);

  Message updateMessage(UUID messageId, MessageUpdateRequest request);

  void deleteMessage(UUID messageId);

}
