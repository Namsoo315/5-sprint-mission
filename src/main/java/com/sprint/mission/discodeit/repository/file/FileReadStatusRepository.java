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

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

@Repository("readStatusRepository")
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileReadStatusRepository implements ReadStatusRepository {
	private final String DIRECTORY = "FileData/READSTATUS";
	private final String EXTENSION = ".ser";

	public FileReadStatusRepository() {
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
	public ReadStatus save(ReadStatus readStatus) {
		Path path = Paths.get(DIRECTORY, readStatus.getReadStatusId() + EXTENSION);
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(readStatus);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return readStatus;
	}

	@Override
	public Optional<ReadStatus> findById(UUID readStatusId) {
		ReadStatus readStatus = null;
		Path path = Paths.get(DIRECTORY, readStatusId.toString() + EXTENSION);

		try (FileInputStream fis = new FileInputStream(path.toFile());
			 ObjectInputStream ois = new ObjectInputStream(fis);) {
			readStatus = (ReadStatus)ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return Optional.ofNullable(readStatus);
	}



	@Override
	public List<ReadStatus> findAll() {
		try (Stream<Path> paths = Files.list(Paths.get(DIRECTORY))) {
			return paths.filter(path -> path.toString().endsWith(EXTENSION))
				.map(filePath -> {
					try (FileInputStream fis = new FileInputStream(filePath.toFile());
						 ObjectInputStream ois = new ObjectInputStream(fis);) {
						return (ReadStatus)ois.readObject();
					} catch (Exception e) {
						throw new RuntimeException("파일 읽기 실패", e);
					}
				}).toList();
		} catch (IOException e) {
			throw new RuntimeException("디렉터리 탐색 실패", e);
		}
	}

	@Override
	public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
		return this.findAll().stream()
			.filter(readStatus -> readStatus.getUserId().equals(userId) && readStatus.getChannelId().equals(channelId))
			.findFirst();
	}

	@Override
	public List<ReadStatus> findAllByUserId(UUID userId) {
		return this.findAll().stream().filter(readStatus -> readStatus.getUserId().equals(userId)).toList();
	}

	@Override
	public List<ReadStatus> findAllByChannelId(UUID channelId) {
		return this.findAll().stream().filter(status -> status.getChannelId().equals(channelId)).toList();
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
		List<ReadStatus> list = this.findAll().stream().filter(readStatus -> readStatus.getChannelId().equals(channelId)).toList();

		for(ReadStatus readStatus : list) {
			this.delete(readStatus.getReadStatusId());
		}
	}

	@Override
	public boolean existsById(UUID id) {
		return Files.exists(Paths.get(DIRECTORY, id.toString() + EXTENSION));
	}
}
