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

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;


public class FileReadStatusRepository implements ReadStatusRepository {
	private final String DIRECTORY;
	private final String EXTENSION;

	public FileReadStatusRepository() {
		this.DIRECTORY = "READSTATUS";
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
	public ReadStatus save(ReadStatus readStatus) {
		boolean isNew = !existsById(readStatus.getReadStatusId());

		Path path = Paths.get(DIRECTORY, readStatus.getReadStatusId() + EXTENSION);
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(readStatus);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (isNew) {
			System.out.println("생성 되었습니다.");
		} else {
			System.out.println("업데이트 되었습니다.");
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
		List<ReadStatus> readStatuses = new ArrayList<>();
		Path directory = Paths.get(DIRECTORY);

		try {
			Files.list(directory)
				.filter(path -> path.toString().endsWith(EXTENSION))
				.forEach(filePath -> {
					try (FileInputStream fis = new FileInputStream(filePath.toFile());
						 ObjectInputStream ois = new ObjectInputStream(fis);) {
						ReadStatus readStatus = (ReadStatus)ois.readObject();
						readStatuses.add(readStatus);
					} catch (Exception e) {
						throw new RuntimeException("파일 읽기 실패",e);
					}
				});
		} catch (IOException e) {
			throw new RuntimeException("디렉터리 탐색 실패", e);
		}
		return readStatuses;
	}

	@Override
	public List<ReadStatus> findAllByUserId(UUID userId) {
		return this.findAll().stream().filter(status -> status.getUserId().equals(userId)).toList();
	}

	@Override
	public List<ReadStatus> findAllByChannelId(UUID channelId) {
		return this.findAll().stream().filter(status -> status.getChannelId().equals(channelId)).toList();
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
	public void deleteByChannelId(UUID channelId) {

	}

	@Override
	public boolean existsById(UUID id) {
		return Files.exists(Paths.get(DIRECTORY, id.toString() + EXTENSION));
	}
}
