package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "첨부파일 관련 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  // [ ] 바이너리 파일을 1개 또는 여러 개 조회할 수 있다.
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<ApiResponse<BinaryContent>> findBinaryContent(
      @PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.findByBinaryContentId(binaryContentId);

    return ResponseEntity.ok(ApiResponse.ok(binaryContent, "파일 조회 완료 [단회]"));
  }

  // Get은 Find의 의미가 있기에 /find를 제거해서 path를 간결화 (
  @GetMapping
  public ResponseEntity<ApiResponse<List<BinaryContent>>> findAllBinaryContent(
      @RequestParam List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);

    return ResponseEntity.ok(ApiResponse.ok(binaryContents, "파일 조회 완료 [다회]"));
  }
}
