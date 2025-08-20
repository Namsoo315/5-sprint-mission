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
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

  private final String directory;
  private final String extension;

  public FileBinaryContentRepository(RepositoryProperties properties) {
    directory = properties.getFileDirectory() + "/BINARY";
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
  public BinaryContent save(BinaryContent binaryContent) {
    Path path = Paths.get(directory, binaryContent.getId() + extension);
    try (FileOutputStream fos = new FileOutputStream(path.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(binaryContent);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return binaryContent;
  }

  @Override
  public Optional<BinaryContent> findById(UUID binaryContentId) {
    BinaryContent binaryContent = null;
    Path path = Paths.get(directory, binaryContentId.toString() + extension);

    try (FileInputStream fis = new FileInputStream(path.toFile());
        ObjectInputStream ois = new ObjectInputStream(fis);) {
      binaryContent = (BinaryContent) ois.readObject();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return Optional.ofNullable(binaryContent);
  }

  @Override
  public List<BinaryContent> findAll() {
    try (Stream<Path> paths = Files.list(Paths.get(directory))) {
      return paths.filter(path -> path.toString().endsWith(extension))
          .map(filePath -> {
            try (FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);) {
              return (BinaryContent) ois.readObject();
            } catch (Exception e) {
              throw new RuntimeException("파일 읽기 실패", e);
            }
          }).toList();
    } catch (IOException e) {
      throw new RuntimeException("디렉터리 탐색 실패", e);
    }
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return List.of();
  }

  @Override
  public void delete(UUID binaryContentId) {
    Path path = Paths.get(directory, binaryContentId.toString() + extension);

    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      throw new RuntimeException(e + "파일 삭제 실패");
    }
  }

  @Override
  public void deleteByAttachmentId(List<UUID> attachmentIds) {
    Path path = null;
    for (UUID attachmentId : attachmentIds) {
      path = Paths.get(directory, attachmentId.toString() + extension);

      try {
        Files.deleteIfExists(path);
      } catch (IOException e) {
        throw new RuntimeException(e + "파일 삭제 실패");
      }
    }
  }

  @Override
  public boolean existsById(UUID binaryContentId) {
    return Files.exists(Paths.get(directory, binaryContentId.toString() + extension));
  }

}
