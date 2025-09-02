package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor

public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;

  @Override
  @Transactional
  public MessageDTO createMessage(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments) throws IOException {

    // 1. 호환성 체크
    User user = userRepository.findById(messageCreateRequest.authorId()).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 회원입니다."));

    Channel channel = channelRepository.findById(messageCreateRequest.channelId()).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 채널입니다."));

    List<BinaryContent> attachmentIds = new ArrayList<>();
    // 1-2. 선택적으로 첨부파일들을 같이 등록함. 있으면 등록 없으면 등록 안함.
    if (attachments != null && !attachments.isEmpty()) {
      for (MultipartFile profile : attachments) {
        if (profile != null && !profile.isEmpty()) {

          BinaryContent content = BinaryContent.builder()
              .fileName(profile.getOriginalFilename())
              .contentType(profile.getContentType())
              .size(profile.getSize())
              .build();
          attachmentIds.add(content);
          BinaryContent save = binaryContentRepository.save(content);
          binaryContentStorage.put(save.getId(), profile.getBytes());
        }
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

    return messageMapper.toDto(message);
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDTO findByMessageId(UUID messageId) {
    Message save = messageRepository.findById(messageId).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 메시지입니다."));
    return messageMapper.toDto(save);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDTO> findAllByChannelId(UUID channelId, Pageable pageable) {
    Page<Message> page = messageRepository.findAllByChannelId(channelId, pageable);

    List<MessageDTO> dtoList = page.getContent().stream()
        .map(messageMapper::toDto)
        .toList();

    return pageResponseMapper.fromPage(page, dtoList);
  }

  @Override
  @Transactional
  public MessageDTO updateMessage(UUID messageId, MessageUpdateRequest request) {
    Message message = messageRepository.findById(messageId).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 메시지입니다."));
    messageRepository.save(message);
    return messageMapper.toDto(message);
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
