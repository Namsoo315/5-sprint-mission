package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.request.BinaryCreateRequest;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContentDTO createBinaryContent(BinaryCreateRequest request);

  BinaryContentDTO findByBinaryContentId(UUID binaryContentId);

  List<BinaryContentDTO> findAllByIdIn(List<UUID> attachmentIds);

  void delete(UUID binaryContentId);
}
