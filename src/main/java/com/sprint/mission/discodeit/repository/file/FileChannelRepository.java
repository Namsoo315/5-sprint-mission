package com.sprint.mission.discodeit.repository.file;

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

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;


public class FileChannelRepository implements ChannelRepository {
	private final String DIRECTORY;
	private final String EXTENSION;

	public FileChannelRepository() {
		this.DIRECTORY = "CHANNEL";
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
	public Channel save(Channel channel) {
		boolean isNew = !existsById(channel.getChannelId());

		Path path = Paths.get(DIRECTORY, channel.getChannelId() + EXTENSION);
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(channel);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (isNew) {
			System.out.println("생성 되었습니다.");
		} else {
			System.out.println("업데이트 되었습니다.");
		}

		return channel;
	}

	@Override
	public Optional<Channel> findById(UUID id) {
		Channel channel = null;

		Path path = Paths.get(DIRECTORY, id.toString() + EXTENSION);

		try (FileInputStream fis = new FileInputStream(path.toFile());
			 ObjectInputStream ois = new ObjectInputStream(fis);) {
			channel = (Channel)ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return Optional.ofNullable(channel);
	}

	@Override
	public List<Channel> findAll() {
		List<Channel> channels = new ArrayList<>();
		Path directory = Paths.get(DIRECTORY);

		try {
			Files.list(directory)
				.filter(path -> path.toString().endsWith(EXTENSION))
				.forEach(filePath -> {
					try (FileInputStream fis = new FileInputStream(filePath.toFile());
						 ObjectInputStream ois = new ObjectInputStream(fis);) {
						Channel channel = (Channel)ois.readObject();
						channels.add(channel);
					} catch (Exception e) {
						throw new RuntimeException("파일 읽기 실패", e);
					}
				});
		} catch (IOException e) {
			throw new RuntimeException("디렉터리 탐색 실패", e);
		}
		return channels;
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
