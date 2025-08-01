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

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

@Repository("userStatusRepository")
public class FileUSRepository implements UserStatusRepository {
	private final String DIRECTORY;
	private final String EXTENSION;

	public FileUSRepository() {
		this.DIRECTORY = "USERSTATUS";
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
	public UserStatus save(UserStatus userStatus) {
		boolean isNew = !existsById(userStatus.getUserStatusId());

		Path path = Paths.get(DIRECTORY, userStatus.getUserStatusId() + EXTENSION);
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(userStatus);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (isNew) {
			System.out.println("생성 되었습니다.");
		} else {
			System.out.println("업데이트 되었습니다.");
		}

		return userStatus;
	}

	@Override
	public Optional<UserStatus> findById(UUID userStatusId) {
		UserStatus userStatus = null;
		Path path = Paths.get(DIRECTORY, userStatusId.toString() + EXTENSION);

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
		List<UserStatus> userStatuses = new ArrayList<>();
		Path directory = Paths.get(DIRECTORY);
		return Optional.empty();
	}

	@Override
	public List<UserStatus> findAll() {
		List<UserStatus> userStatuses = new ArrayList<>();
		Path directory = Paths.get(DIRECTORY);

		try {
			Files.list(directory)
				.filter(path -> path.toString().endsWith(EXTENSION))
				.forEach(filePath -> {
					try (FileInputStream fis = new FileInputStream(filePath.toFile());
						 ObjectInputStream ois = new ObjectInputStream(fis);) {
						UserStatus userStatus = (UserStatus)ois.readObject();
						userStatuses.add(userStatus);
					} catch (Exception e) {
						throw new RuntimeException("파일 읽기 실패",e);
					}
				});
		} catch (IOException e) {
			throw new RuntimeException("디렉터리 탐색 실패", e);
		}
		return userStatuses;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void delete(UUID userStatusId) {
		Path path = Paths.get(DIRECTORY, userStatusId.toString() + EXTENSION);

		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			throw new RuntimeException(e + "파일 삭제 실패");
		}
	}

	@Override
	public void deleteByUserId(UUID userId) {
		// 기능 추가해야함.
	}

	@Override
	public boolean existsById(UUID userStatusId) {
		return Files.exists(Paths.get(DIRECTORY, userStatusId.toString() + EXTENSION));
	}
}
