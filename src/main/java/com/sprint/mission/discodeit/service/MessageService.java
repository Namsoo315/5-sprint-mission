package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  MessageDTO createMessage(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments) throws IOException;

  MessageDTO findByMessageId(UUID messageId);

  PageResponse<MessageDTO> findAllByChannelId(UUID channelId, Pageable pageable);

  MessageDTO updateMessage(UUID messageId, MessageUpdateRequest request);

  void deleteMessage(UUID messageId);

}
