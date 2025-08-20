package com.sprint.mission.discodeit.repository.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileMessageRepository implements MessageRepository {

  private final String directory;
  private final String extension;

  public FileMessageRepository(RepositoryProperties properties) {
    directory = properties.getFileDirectory() + "/MESSAGE";
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
  public Message save(Message message) {
    Path path = Paths.get(directory, message.getId() + extension);
    try (FileOutputStream fos = new FileOutputStream(path.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(message);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return message;
  }

  @Override
  public Optional<Message> findById(UUID messageId) {
    Message message = null;
    Path path = Paths.get(directory, messageId.toString() + extension);

    try (FileInputStream fis = new FileInputStream(path.toFile());
        ObjectInputStream ois = new ObjectInputStream(fis);) {
      message = (Message) ois.readObject();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return Optional.ofNullable(message);
  }

  @Override
  public List<Message> findAll() {
    try (Stream<Path> paths = Files.list(Paths.get(directory))) {
      return paths.filter(path -> path.toString().endsWith(extension))
          .map(filePath -> {
            try (FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);) {
              return (Message) ois.readObject();
            } catch (Exception e) {
              throw new RuntimeException("파일 읽기 실패", e);
            }
          }).toList();
    } catch (IOException e) {
      throw new RuntimeException("디렉터리 탐색 실패", e);
    }
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return this.findAll().stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public Optional<Message> latestMessageByChannelId(UUID channelId) {
    return this.findAll().stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .max(Comparator.comparing(Message::getCreatedAt));
  }

  @Override
  public void delete(UUID id) {
    Path path = Paths.get(directory, id.toString() + extension);

    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      throw new RuntimeException(e + "파일 삭제 실패");
    }
  }

  @Override
  public void deleteByChannelId(UUID channelId) {
    List<Message> list = this.findAll().stream()
        .filter(messages -> messages.getChannelId().equals(channelId)).toList();

    for (Message messages : list) {
      this.delete(messages.getId());
    }
  }

  @Override
  public boolean existsById(UUID messageId) {
    return Files.exists(Paths.get(directory, messageId.toString() + extension));
  }
}
