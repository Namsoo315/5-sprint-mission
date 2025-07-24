package com.sprint.mission.discodeit.repository.file;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

public class FileMessageRepository implements MessageRepository {
	private final String DIRECTORY;
	private final String EXTENSION;

	public FileMessageRepository() {
		this.DIRECTORY = "MESSAGE";
		this.EXTENSION = ".ser";

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
		boolean isNew = !existsById(message.getMessageId());

		Path path = Paths.get(DIRECTORY, message.getMessageId() + EXTENSION);
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(message);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (isNew) {
			System.out.println("생성 되었습니다.");
		} else {
			System.out.println("업데이트 되었습니다.");
		}

		return message;
	}

	@Override
	public Optional<Message> findById(UUID id) {
		Message message = null;
		Path path = Paths.get(DIRECTORY, id.toString() + EXTENSION);

		try (FileInputStream fis = new FileInputStream(path.toFile());
			 ObjectInputStream ois = new ObjectInputStream(fis);) {
			message = (Message) ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return Optional.ofNullable(message);
	}

	@Override
	public List<Message> findAll() {
		List<Message> messages = new ArrayList<>();
		Path path = Paths.get(DIRECTORY);

		try (FileInputStream fis = new FileInputStream(path.toFile());
			 ObjectInputStream ois = new ObjectInputStream(fis);) {
			while (true) {
				Message message = (Message) ois.readObject();
				messages.add(message);
			}
		} catch (EOFException e) {
			// 파일의 끝
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return messages;
	}

	@Override
	public long count() {
		return 0;
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
	public boolean existsById(UUID id) {
		return Files.exists(Paths.get(DIRECTORY, id.toString() + EXTENSION));
	}
}
