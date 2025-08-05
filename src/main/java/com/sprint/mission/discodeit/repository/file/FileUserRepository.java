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

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;


public class FileUserRepository implements UserRepository {
	private final String DIRECTORY = "FileData/USER";
	private final String EXTENSION = ".ser";

	public FileUserRepository() {
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
		Path path = Paths.get(DIRECTORY, user.getUserId() + EXTENSION);
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(user);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return user;
	}

	@Override
	public Optional<User> findById(UUID userId) {
		User user = null;
		Path path = Paths.get(DIRECTORY, userId.toString() + EXTENSION);

		try (FileInputStream fis = new FileInputStream(path.toFile());
			 ObjectInputStream ois = new ObjectInputStream(fis);) {
			user = (User)ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return Optional.ofNullable(user);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return this.findAll().stream()
			.filter(user -> user.getUsername().equals(username))
			.findFirst();
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return this.findAll().stream()
			.filter(user -> user.getEmail().equals(email))
			.findFirst();
	}

	@Override
	public List<User> findAll() {
		try (Stream<Path> paths = Files.list(Paths.get(DIRECTORY))) {
			return paths.filter(path -> path.toString().endsWith(EXTENSION))
				.map(filePath -> {
					try (FileInputStream fis = new FileInputStream(filePath.toFile());
						 ObjectInputStream ois = new ObjectInputStream(fis);) {
						return (User)ois.readObject();
					} catch (Exception e) {
						throw new RuntimeException("파일 읽기 실패", e);
					}
				}).toList();
		} catch (IOException e) {
			throw new RuntimeException("디렉터리 탐색 실패", e);
		}
	}

	@Override
	public void delete(UUID userId) {
		Path path = Paths.get(DIRECTORY, userId.toString() + EXTENSION);

		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			throw new RuntimeException(e + "파일 삭제 실패");
		}
	}

	@Override
	public boolean existsById(UUID userId) {
		return Files.exists(Paths.get(DIRECTORY, userId.toString() + EXTENSION));
	}
}
