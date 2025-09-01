package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  @Transactional
  public BinaryContent createBinaryContent(BinaryContentDTO request) {
    BinaryContent binaryContent = BinaryContent.builder()
        .fileName(request.fileName())
        .contentType(request.contentType())
        .size(request.size())
        .bytes(request.bytes())
        .build();
    binaryContentRepository.save(binaryContent);

    return binaryContent;
  }

  @Override
  @Transactional
  public BinaryContent findByBinaryContentId(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 파일입니다."));
  }

  @Override
  @Transactional
  public List<BinaryContent> findAllByIdIn(List<UUID> attachmentIds) {
    return binaryContentRepository.findAllById(attachmentIds);
  }

  @Override
  @Transactional
  public void delete(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new NoSuchElementException("존재하지 않는 파일입니다.");
    }
    binaryContentRepository.deleteById(binaryContentId);
  }
}
