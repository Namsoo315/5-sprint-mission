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
import java.util.stream.Stream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

@Repository("channelRepository")
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileChannelRepository implements ChannelRepository {
	private static final String DIRECTORY = "FileData/CHANNEL";
	private static final String EXTENSION = ".ser";

	public FileChannelRepository() {
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
		Path path = Paths.get(DIRECTORY, channel.getChannelId() + EXTENSION);
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(channel);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return channel;
	}

	@Override
	public Optional<Channel> findById(UUID channelId) {
		Channel channel = null;

		Path path = Paths.get(DIRECTORY, channelId.toString() + EXTENSION);

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
		try (Stream<Path> paths = Files.list(Paths.get(DIRECTORY))) {
			return paths.filter(path -> path.toString().endsWith(EXTENSION))
				.map(filePath -> {
					try (FileInputStream fis = new FileInputStream(filePath.toFile());
						 ObjectInputStream ois = new ObjectInputStream(fis);) {
						return (Channel)ois.readObject();

					} catch (Exception e) {
						throw new RuntimeException("파일 읽기 실패", e);
					}
				}).toList();
		} catch (IOException e) {
			throw new RuntimeException("디렉터리 탐색 실패", e);
		}
	}

	@Override
	public void delete(UUID channelId) {
		Path path = Paths.get(DIRECTORY, channelId.toString() + EXTENSION);

		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			throw new RuntimeException(e + "파일 삭제 실패");
		}
	}

	@Override
	public boolean existsById(UUID channelId) {
		return Files.exists(Paths.get(DIRECTORY, channelId.toString() + EXTENSION));
	}
}
