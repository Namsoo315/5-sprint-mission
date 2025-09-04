package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Page<Message> findAllByChannelId(UUID channelId, Pageable pageable);

  Slice<Message> findAllByChannelIdAndCreatedAtAfter(UUID channelId, Instant cursor,
      Pageable pageable);

  Optional<Message> findTop1ByChannelIdOrderByCreatedAtDesc(UUID channelId);

  void deleteByChannelId(UUID channelId);

}
