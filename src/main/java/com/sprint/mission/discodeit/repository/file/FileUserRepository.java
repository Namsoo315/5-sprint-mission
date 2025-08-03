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

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

public class FileUserRepository implements UserRepository {
	private final String DIRECTORY;
	private final String EXTENSION;

	public FileUserRepository() {
		this.DIRECTORY = "USER";
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
	public User save(User user) {
		boolean isNew = !existsById(user.getUserId());

		Path path = Paths.get(DIRECTORY, user.getUserId() + EXTENSION);
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(user);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (isNew) {
			System.out.println("생성 되었습니다.");
		} else {
			System.out.println("업데이트 되었습니다.");
		}

		return user;
	}

	@Override
	public Optional<User> findById(UUID id) {
		User user = null;
		Path path = Paths.get(DIRECTORY, id.toString() + EXTENSION);

		try (FileInputStream fis = new FileInputStream(path.toFile());
			 ObjectInputStream ois = new ObjectInputStream(fis);) {
			user = (User)ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return Optional.ofNullable(user);
	}

	@Override
	public List<User> findAll() {
		List<User> users = new ArrayList<>();
		Path directory = Paths.get(DIRECTORY);

		try {
			Files.list(directory)
				.filter(path -> path.toString().endsWith(EXTENSION))
				.forEach(filePath -> {
					try (FileInputStream fis = new FileInputStream(filePath.toFile());
						 ObjectInputStream ois = new ObjectInputStream(fis);) {
						User user = (User)ois.readObject();
						users.add(user);
					} catch (Exception e) {
						throw new RuntimeException("파일 읽기 실패",e);
					}
				});
		} catch (IOException e) {
			throw new RuntimeException("디렉터리 탐색 실패", e);
		}
		return users;
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
