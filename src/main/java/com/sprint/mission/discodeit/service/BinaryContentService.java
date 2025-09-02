package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binary.BinaryCreateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;

public interface BinaryContentService {

  BinaryContentDTO createBinaryContent(BinaryCreateRequest request);

  BinaryContentDTO findByBinaryContentId(UUID binaryContentId);

  List<BinaryContentDTO> findAllByIdIn(List<UUID> attachmentIds);

  void delete(UUID binaryContentId);
}
