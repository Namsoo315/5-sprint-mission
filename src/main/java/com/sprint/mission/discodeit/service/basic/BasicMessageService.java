package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  @Transactional
  public Message createMessage(MessageCreateRequest messageCreateRequest,
      List<BinaryContentDTO> binaryContentDTO) {

    // 1. 호환성 체크
    User user = userRepository.findById(messageCreateRequest.authorId()).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 회원입니다."));

    Channel channel = channelRepository.findById(messageCreateRequest.channelId()).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 채널입니다."));

    List<BinaryContent> attachmentIds = new ArrayList<>();
    // 1-2. 선택적으로 첨부파일들을 같이 등록함. 있으면 등록 없으면 등록 안함.
    if (binaryContentDTO != null && !binaryContentDTO.isEmpty()) {
      for (BinaryContentDTO dto : binaryContentDTO) {
        if (dto.bytes() == null || dto.bytes().length == 0) {
          continue;
        }

        BinaryContent binaryContent = BinaryContent.builder()
            .fileName(dto.fileName())
            .contentType(dto.contentType())
            .size(dto.size())
            .bytes(dto.bytes())
            .build();
        attachmentIds.add(binaryContent);
        binaryContentRepository.save(binaryContent);
      }
    }

    // 2. 메시지 생성
    Message message = Message.builder()
        .author(user)
        .channel(channel)
        .content(messageCreateRequest.content())
        .attachments(attachmentIds)
        .build();
    messageRepository.save(message);

    return message;
  }

  @Override
  @Transactional(readOnly = true)
  public Message findByMessageId(UUID messageId) {
    return messageRepository.findById(messageId).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 메시지입니다."));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId);
  }

  @Override
  @Transactional
  public Message updateMessage(UUID messageId, MessageUpdateRequest request) {
    Message message = messageRepository.findById(messageId).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 메시지입니다."));

    return messageRepository.save(message);
  }

  @Override
  @Transactional(readOnly = true)
  public void deleteMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 메시지입니다."));

    binaryContentRepository.deleteAll(message.getAttachments());

    messageRepository.deleteById(messageId);
  }
}
