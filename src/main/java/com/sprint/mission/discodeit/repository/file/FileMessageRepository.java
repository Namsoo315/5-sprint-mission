package com.sprint.mission.discodeit.repository.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;

public class FileMessageRepository implements MessageRepository {
	private final String DIRECTORY = "FileData/MESSAGE";
	;
	private final String EXTENSION = ".ser";
	;

	public FileMessageRepository() {
		Path path = Paths.get(DIRECTORY);
		if (!path.toFile().exists()) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public Message save(Message message) {
		Path path = Paths.get(DIRECTORY, message.getMessageId() + EXTENSION);
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(message);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return message;
	}

	@Override
	public Optional<Message> findById(UUID messageId) {
		Message message = null;
		Path path = Paths.get(DIRECTORY, messageId.toString() + EXTENSION);

		try (FileInputStream fis = new FileInputStream(path.toFile());
			 ObjectInputStream ois = new ObjectInputStream(fis);) {
			message = (Message)ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return Optional.ofNullable(message);
	}

	@Override
	public List<Message> findAll() {
		try (Stream<Path> paths = Files.list(Paths.get(DIRECTORY))) {
			return paths.filter(path -> path.toString().endsWith(EXTENSION))
				.map(filePath -> {
					try (FileInputStream fis = new FileInputStream(filePath.toFile());
						 ObjectInputStream ois = new ObjectInputStream(fis);) {
						return (Message)ois.readObject();
					} catch (Exception e) {
						throw new RuntimeException("파일 읽기 실패", e);
					}
				}).toList();
		} catch (IOException e) {
			throw new RuntimeException("디렉터리 탐색 실패", e);
		}
	}

	@Override
	public List<Message> findAllByChannelId(UUID channelId) {
		return this.findAll().stream()
			.filter(message -> message.getChannelId().equals(channelId))
			.toList();
	}

	@Override
	public Instant LatestMessageByChannelId(UUID channelId) {
		return this.findAll().stream()
			.filter(message -> message.getChannelId().equals(channelId))
			.max(Comparator.comparing(Message::getCreatedAt))
			.map(Message::getCreatedAt)
			.orElse(null);
	}

	@Override
	public void delete(UUID id) {
		Path path = Paths.get(DIRECTORY, id.toString() + EXTENSION);

		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			throw new RuntimeException(e + "파일 삭제 실패");
		}
	}

	@Override
	public void deleteByChannelId(UUID channelId) {
		List<Message> list = this.findAll().stream().filter(messages -> messages.getChannelId().equals(channelId)).toList();

		for(Message messages : list) {
			this.delete(messages.getMessageId());
		}
	}

	@Override
	public boolean existsById(UUID messageId) {
		return Files.exists(Paths.get(DIRECTORY, messageId.toString() + EXTENSION));
	}
}
