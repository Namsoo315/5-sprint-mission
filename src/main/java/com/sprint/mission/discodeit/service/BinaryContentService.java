package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContentDTO findByBinaryContentId(UUID binaryContentId);

  List<BinaryContentDTO> findAllByIdIn(List<UUID> attachmentIds);

  void delete(UUID binaryContentId);
}
