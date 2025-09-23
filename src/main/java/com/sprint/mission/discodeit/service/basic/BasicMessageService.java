package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentSaveFailedException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.InvalidMessageParameterException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
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
      List<MultipartFile> attachments) {
    // 1. 호환성 체크
    User user = userRepository.findById(messageCreateRequest.authorId()).orElseThrow(() -> {
      log.warn("존재하지 않는 회원 메시지 생성 시도");
      return new UserNotFoundException();
    });

    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> {
          log.warn("존재하지 않는 채널 메시지 생성 시도");
          return new ChannelNotFoundException();
        });

    // 1-2. 선택적으로 첨부파일들을 같이 등록함. 있으면 등록 없으면 등록 안함.
    List<BinaryContent> attachmentIds = attachments == null ? List.of()
        : attachments.stream().filter(file -> file != null && !file.isEmpty()).map(file -> {
          BinaryContent content = BinaryContent.builder().fileName(file.getOriginalFilename())
              .contentType(file.getContentType()).size(file.getSize()).build();

          BinaryContent saved = binaryContentRepository.save(content);
          try {
            binaryContentStorage.save(saved.getId(), file.getBytes());
          } catch (IOException e) {
            throw BinaryContentSaveFailedException.withMessage(e.getMessage());
          }
          return saved;
        }).toList();

    Message message = Message.builder().author(user).channel(channel)
        .content(messageCreateRequest.content()).attachments(attachmentIds).build();

    log.info("생성할 메시지 내용='{}'", message.getContent());
    try {
      Message save = messageRepository.save(message);
      log.debug("메시지 생성 완료 아이디='{}'", save.getId());
      return messageMapper.toDto(save);
    } catch (Exception e) {
      log.error("메시지 생성 실패 내용='{}'", message.getContent(), e);
      throw InvalidMessageParameterException.withMessage(e.getMessage());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDTO findByMessageId(UUID messageId) {
    Message save = messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);
    return messageMapper.toDto(save);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDTO> findAllByChannelId(UUID channelId, Instant cursor,
      Pageable pageable) {
    channelRepository.findById(channelId).orElseThrow(MessageNotFoundException::new);

    Slice<Message> slice =
        (cursor != null) ? messageRepository.findAllByChannelIdWithAuthorAndAttachments(channelId,
            cursor, pageable)
            : messageRepository.findAllByChannelIdWithPageable(channelId, pageable);

    List<MessageDTO> dtoList = slice.getContent().stream().map(messageMapper::toDto).toList();

    return pageResponseMapper.fromSlice(slice, dtoList);
  }

  @Override
  @Transactional
  public MessageDTO updateMessage(UUID messageId, MessageUpdateRequest request) {
    Message message = messageRepository.findById(messageId).orElseThrow(() -> {
      log.warn("존재하지 않는 메시지 업데이트 시도");
      return new NoSuchElementException("존재하지 않는 메시지입니다.");
    });

    log.info("업데이트할 메시지 내용='{}'", message.getContent());

    // 실제 업데이트 로직
    Message updateMessage = message.toBuilder()
        .content(request.newContent() != null ? request.newContent() : message.getContent())
        .build();

    try {
      Message save = messageRepository.save(updateMessage);
      log.debug("업데이트된 메시지 내용='{}'", save.getContent());
      return messageMapper.toDto(save);
    } catch (Exception e) {
      log.error("메시지 업데이트 실패 내용='{}'", message.getContent(), e);
      throw InvalidMessageParameterException.withMessage(e.getMessage());
    }
  }

  @Override
  @Transactional
  public void deleteMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId).orElseThrow(() -> {
      log.warn("존재하지 않는 메시지 삭제 시도");
      return new NoSuchElementException("존재하지 않는 메시지입니다.");
    });

    log.info("삭제할 메시지 내용='{}'", message.getContent());

    try {
      binaryContentRepository.deleteAll(message.getAttachments());
      messageRepository.deleteById(messageId);
      log.debug("메시지 삭제 완료 아이디='{}'", message.getId());
    } catch (Exception e) {
      log.error("메시지 삭제 실패", e);
      throw InvalidMessageParameterException.withMessage(e.getMessage());
    }
  }
}
