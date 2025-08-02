package com.sprint.mission.discodeit.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import lombok.RequiredArgsConstructor;

@Service("messageService")
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
	private final MessageRepository messageRepository;
	private final ChannelRepository	channelRepository;
	private final UserRepository userRepository;
	private final BinaryContentRepository binaryContentRepository;

	@Override
	public Message createMessage(MessageCreateRequest request) {

		if (userRepository.findById(request.getUserId()).isPresent()) {
			throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
		}
		if (channelRepository.findById(request.getChannelId()).isPresent()) {
			throw new IllegalArgumentException("채널방을 찾을 수 없습니다.");
		}

		Message message = new Message(request.getUserId(),
			request.getChannelId(),
			request.getMessage(),
			request.getAttachmentIds() != null ? request.getAttachmentIds() : new ArrayList<>()	// 삼항 연산자 AttachmentIds가 있을 경우에만.
		);
		messageRepository.save(message);

		return message;
	}

	@Override
	public List<Message> findByUserIdAndChannelId(UUID userId, UUID channelId) {
		List<Message> result = new ArrayList<>();
		for (Message m : messageRepository.findAll()) {
			if (m.getUserId().equals(userId) && m.getChannelId().equals(channelId)) {
				result.add(m);
			}
		}

		return result;
	}

	@Override
	public Optional<Message> findByMessageId(UUID messageId) {return messageRepository.findById(messageId);
	}

	public List<Message> findAllByChannelId(UUID channelId) {
		List<Message> result = new ArrayList<>();
		for (Message m : messageRepository.findAll()) {
			if(m.getChannelId().equals(channelId)) {
				result.add(m);
			}
		}
		return result;
	}

	@Override
	public void updateMessage(MessageUpdateRequest request) {
		Message message = messageRepository.findById(request.getMessageId()).orElse(null);

		if(message == null) {
			throw new IllegalArgumentException("메시지를 찾을 수 없습니다.");
		}
		// update를 하는거면 첨부파일 업데이트 해야하나? (본인은 안해도 된다고 생각함.)
		message.update(request.getNewContent());
		messageRepository.save(message);
	}

	@Override
	public void deleteMessage(UUID messageId) {
		messageRepository.delete(messageId);
		binaryContentRepository.deleteByMessageId(messageId);
	}
}
