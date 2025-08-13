package com.sprint.mission.discodeit.repository.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
@Repository
public class FileUserStatusRepository implements UserStatusRepository {
	private final String directory;
	private final String extension;

	public FileUserStatusRepository(RepositoryProperties properties) {
		directory = properties.getFileDirectory() + "/USERSTATUS";
		extension = properties.getExtension();

		Path path = Paths.get(directory);
		if (!path.toFile().exists()) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public UserStatus save(UserStatus userStatus) {
		Path path = Paths.get(directory, userStatus.getUserStatusId() + extension);
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(userStatus);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return userStatus;
	}

	@Override
	public Optional<UserStatus> findById(UUID userStatusId) {
		UserStatus userStatus = null;
		Path path = Paths.get(directory, userStatusId.toString() + extension);

		try (FileInputStream fis = new FileInputStream(path.toFile());
			 ObjectInputStream ois = new ObjectInputStream(fis);) {
			userStatus = (UserStatus)ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return Optional.ofNullable(userStatus);
	}

	@Override
	public Optional<UserStatus> findByUserId(UUID userId) {
		return this.findAll().stream()
			.filter(userStatus -> userStatus.getUserId().equals(userId))
			.findFirst();
	}

	@Override
	public List<UserStatus> findAll() {
		try (Stream<Path> paths = Files.list(Paths.get(directory))) {
			return paths.filter(path -> path.toString().endsWith(extension))
				.map(filePath -> {
					try (FileInputStream fis = new FileInputStream(filePath.toFile());
						 ObjectInputStream ois = new ObjectInputStream(fis);) {
						return (UserStatus)ois.readObject();
					} catch (Exception e) {
						throw new RuntimeException("파일 읽기 실패", e);
					}
				}).toList();
		} catch (IOException e) {
			throw new RuntimeException("디렉터리 탐색 실패", e);
		}
	}

	@Override
	public void delete(UUID userStatusId) {
		Path path = Paths.get(directory, userStatusId.toString() + extension);

		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			throw new RuntimeException(e + "파일 삭제 실패");
		}
	}

	@Override
	public void deleteByUserId(UUID userId) {
		List<UserStatus> list = this.findAll().stream().filter(status -> status.getUserId().equals(userId)).toList();

		for(UserStatus status : list) {
			this.delete(status.getUserStatusId());
		}
	}

	@Override
	public boolean existsById(UUID userStatusId) {
		return Files.exists(Paths.get(directory, userStatusId.toString() + extension));
	}
}
