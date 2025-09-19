package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.request.BinaryCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @Transactional
  public BinaryContentDTO createBinaryContent(BinaryCreateRequest request) {
    BinaryContent binaryContent = BinaryContent.builder()
        .fileName(request.fileName())
        .contentType(request.contentType())
        .size(request.size())
        .build();

    BinaryContent save = binaryContentRepository.save(binaryContent);
    binaryContentStorage.save(save.getId(), request.bytes());

    return binaryContentMapper.toDto(save);
  }

  @Override
  @Transactional(readOnly = true)
  public BinaryContentDTO findByBinaryContentId(UUID binaryContentId) {
    BinaryContent save = binaryContentRepository.findById(binaryContentId).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 파일입니다."));
    return binaryContentMapper.toDto(save);

  }

  @Override
  @Transactional(readOnly = true)
  public List<BinaryContentDTO> findAllByIdIn(List<UUID> attachmentIds) {
    List<BinaryContent> saves = binaryContentRepository.findAllById(attachmentIds);
    return binaryContentMapper.toDto(saves);
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
