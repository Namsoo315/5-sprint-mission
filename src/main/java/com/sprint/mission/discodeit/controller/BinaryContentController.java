package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "첨부파일 관련 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;


  // [ ] 바이너리 파일을 1개 조회
  @Operation(summary = "바이너리 파일 단건 조회 API", responses = {
      @ApiResponse(responseCode = "200", description = "바이너리 파일 조회 성공"),
      @ApiResponse(responseCode = "404", description = "파일 ID를 찾을 수 없습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDTO> findBinaryContent(@PathVariable UUID binaryContentId) {
    BinaryContentDTO binaryContent = binaryContentService.findByBinaryContentId(binaryContentId);
    return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
  }

  // [ ] 바이너리 파일을 여러 개 조회
  @Operation(summary = "바이너리 파일 다건 조회 API", responses = {
      @ApiResponse(responseCode = "200", description = "바이너리 파일 조회 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터입니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping
  public ResponseEntity<List<BinaryContentDTO>> findAllBinaryContent(
      @RequestParam List<UUID> binaryContentIds) {
    List<BinaryContentDTO> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.status(HttpStatus.OK).body(binaryContents);
  }


  @Operation(summary = "바이너리 파일 다운로드 API", responses = {
      @ApiResponse(responseCode = "200", description = "바이너리 다운 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터입니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<Resource> downloadBinaryContent(
      @PathVariable UUID binaryContentId) {
    BinaryContentDTO binaryContent = binaryContentService.findByBinaryContentId(binaryContentId);
    return binaryContentStorage.download(binaryContent);
  }
}
