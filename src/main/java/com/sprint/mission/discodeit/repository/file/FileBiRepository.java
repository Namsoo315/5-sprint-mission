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

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryRepository;

@Repository("binaryContentRepository")
public class FileBiRepository implements BinaryRepository {
	private final String DIRECTORY;
	private final String EXTENSION;

	public FileBiRepository() {
		this.DIRECTORY = "BINARY";
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
	public BinaryContent save(BinaryContent binaryContent) {
		boolean isNew = !existsById(binaryContent.getBinaryContentId());

		Path path = Paths.get(DIRECTORY, binaryContent.getBinaryContentId() + EXTENSION);
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(binaryContent);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (isNew) {
			System.out.println("생성 되었습니다.");
		} else {
			System.out.println("업데이트 되었습니다.");
		}

		return binaryContent;
	}

	@Override
	public Optional<BinaryContent> findById(UUID binaryContentId) {
		BinaryContent binaryContent = null;
		Path path = Paths.get(DIRECTORY, binaryContentId.toString() + EXTENSION);

		try (FileInputStream fis = new FileInputStream(path.toFile());
			 ObjectInputStream ois = new ObjectInputStream(fis);) {
			binaryContent = (BinaryContent)ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return Optional.ofNullable(binaryContent);
	}

	@Override
	public List<BinaryContent> findAll() {
		List<BinaryContent> binaryContents = new ArrayList<>();
		Path directory = Paths.get(DIRECTORY);

		try {
			Files.list(directory)
				.filter(path -> path.toString().endsWith(EXTENSION))
				.forEach(filePath -> {
					try (FileInputStream fis = new FileInputStream(filePath.toFile());
						 ObjectInputStream ois = new ObjectInputStream(fis);) {
						BinaryContent binaryContent = (BinaryContent)ois.readObject();
						binaryContents.add(binaryContent);
					} catch (Exception e) {
						throw new RuntimeException("파일 읽기 실패",e);
					}
				});
		} catch (IOException e) {
			throw new RuntimeException("디렉터리 탐색 실패", e);
		}
		return binaryContents;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void delete(UUID binaryContentId) {
		Path path = Paths.get(DIRECTORY, binaryContentId.toString() + EXTENSION);

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
	public void deleteByMessageId(UUID messageId) {

	}

	@Override
	public boolean existsById(UUID binaryContentId) {
		return Files.exists(Paths.get(DIRECTORY, binaryContentId.toString() + EXTENSION));
	}

}
