package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.NotificationDTO;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @Operation(summary = "알림 조회", responses = {
      @ApiResponse(responseCode = "200", description = "알림 조회 성공"),
      @ApiResponse(responseCode = "401", description = "ErrorResponse"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping
  public ResponseEntity<List<NotificationDTO>> findNotifications() {
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    List<NotificationDTO> notifications =
        notificationService.findNotifications(userDetails.getUserDTO().id());
    return ResponseEntity.status(HttpStatus.OK).body(notifications);
  }

  @Operation(summary = "알림 확인", responses = {
      @ApiResponse(responseCode = "204", description = "확인 완료"),
      @ApiResponse(responseCode = "401", description = "401 ErrorResponse"),
      @ApiResponse(responseCode = "403", description = "403 ErrorResponse"),
      @ApiResponse(responseCode = "404", description = "404 ErrorResponse"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> checkNotification(@PathVariable UUID notificationId) {
    notificationService.checkNotifications(notificationId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

  }
}
