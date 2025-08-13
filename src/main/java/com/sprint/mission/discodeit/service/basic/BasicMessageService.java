package com.sprint.mission.discodeit.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
	private final MessageRepository messageRepository;
	private final ChannelRepository channelRepository;
	private final UserRepository userRepository;
	private final BinaryContentRepository binaryContentRepository;

	@Override
	public Message createMessage(MessageCreateRequest messageCreateRequest, List<BinaryContentDTO> binaryContentDTO) {

		// 1. 호환성 체크
		if (userRepository.findById(messageCreateRequest.getUserId()).isEmpty()) {
			throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
		}
		if (channelRepository.findById(messageCreateRequest.getChannelId()).isEmpty()) {
			throw new IllegalArgumentException("채널방을 찾을 수 없습니다.");
		}

		List<UUID> attachmentIds = new ArrayList<>();
		// 1-2. 선택적으로 첨부파일들을 같이 등록함. 있으면 등록 없으면 등록 안함.
		if (binaryContentDTO != null && !binaryContentDTO.isEmpty()) {
			for (BinaryContentDTO dto : binaryContentDTO) {
				if(dto.getBinaryContent() == null || dto.getBinaryContent().length == 0)
					continue;

				BinaryContent binaryContent = new BinaryContent(dto.getFileName(), dto.getContentType(), dto.getSize(),
					dto.getBinaryContent());
				attachmentIds.add(binaryContent.getBinaryContentId());
				binaryContentRepository.save(binaryContent);
			}
		}

		// 2. 메시지 생성
		Message message = new Message(messageCreateRequest.getUserId(), messageCreateRequest.getChannelId(),
			messageCreateRequest.getMessage(), attachmentIds);
		messageRepository.save(message);

		return message;
	}

	@Override
	public Optional<Message> findByMessageId(UUID messageId) {
		return messageRepository.findById(messageId);
	}

	public List<Message> findAllByChannelId(UUID channelId) {
		return messageRepository.findAllByChannelId(channelId);
	}

	@Override
	public void updateMessage(MessageUpdateRequest request) {
		Message message = messageRepository.findById(request.getMessageId()).orElseThrow(
			() -> new IllegalArgumentException("메시지 아이디가 존재하지 않습니다."));

		message.update(request.getNewContent());
		messageRepository.save(message);
	}

	@Override
	public void deleteMessage(UUID messageId) {
		Message message = messageRepository.findById(messageId).orElseThrow(
			() -> new IllegalArgumentException("메시지가 존재하지 않습니다."));

		binaryContentRepository.deleteByAttachmentId(message.getAttachmentIds());

		messageRepository.delete(messageId);
	}
}
