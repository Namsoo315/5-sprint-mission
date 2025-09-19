package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  @Query("SELECT m FROM Message m " + "JOIN FETCH m.author " +
      "LEFT JOIN FETCH m.attachments " + "WHERE m.channel.id = :channelId")
  Slice<Message> findAllByChannelIdWithPageable(@Param("channelId") UUID channelId,
      Pageable pageable);

  @Query("SELECT m FROM Message m JOIN FETCH m.author LEFT JOIN FETCH m.attachments " +
      "WHERE m.channel.id = :channelId AND m.createdAt > :cursor")
  Slice<Message> findAllByChannelIdWithAuthorAndAttachments(@Param("channelId") UUID channelId,
      @Param("cursor") Instant cursor,
      Pageable pageable);

  // 자동으로 결과를 1개만 가져오도록 강제함.
  @Query("SELECT m.createdAt FROM Message m WHERE m.channel.id = :channleId "
      + "ORDER BY m.createdAt DESC LIMIT 1")
  Optional<Message> findLastMessageAtByChannelId(@Param("channelId") UUID channelId);

  void deleteByChannelId(UUID channelId);

}
