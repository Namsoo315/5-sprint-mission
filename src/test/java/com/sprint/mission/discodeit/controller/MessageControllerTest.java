package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MessageController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;

  @MockitoBean
  private JpaMetamodelMappingContext jpaMetamodelMappingContext; // 가상의 jpa을 실행해줄 환경

  @Test
  @DisplayName("메시지 전송 (binaryContent 없음)")
  void sendMessageWithoutBinaryContent() throws Exception {
    UUID messageId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(userId, channelId,
        "Hello World");

    // JSON part 이름 반드시 컨트롤러 @RequestPart 이름과 일치
    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest",  // ✅ 수정
        "",
        "application/json",
        objectMapper.writeValueAsBytes(messageCreateRequest)
    );

    MessageDTO dto = MessageDTO.builder()
        .id(messageId)
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .author(UserDTO.builder().id(userId).build())
        .channelId(channelId)
        .content(messageCreateRequest.content())
        .build();

    given(messageService.createMessage(any(MessageCreateRequest.class), isNull()))
        .willReturn(dto);

    mockMvc.perform(multipart("/api/messages")
            .file(messagePart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("Hello World"));

    verify(messageService).createMessage(any(MessageCreateRequest.class), isNull());
  }

  @Test
  @DisplayName("메시지 전송 (여러 첨부파일 포함)")
  void sendMessageWithAttachments() throws Exception {
    UUID messageId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(userId, channelId,
        "Hello with Attachments");

    // JSON part
    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest",  // 컨트롤러 @RequestPart 이름과 일치
        "",
        "application/json",
        objectMapper.writeValueAsBytes(messageCreateRequest)
    );

    // 첨부파일 여러 개
    MockMultipartFile attachment1 = new MockMultipartFile(
        "attachments",  // 컨트롤러 @RequestPart 이름과 일치
        "file1.png",
        "image/png",
        "fake image content 1".getBytes()
    );

    MockMultipartFile attachment2 = new MockMultipartFile(
        "attachments",
        "file2.png",
        "image/png",
        "fake image content 2".getBytes()
    );

    MessageDTO dto = MessageDTO.builder()
        .id(messageId)
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .author(UserDTO.builder().id(userId).build())
        .channelId(channelId)
        .content(messageCreateRequest.content())
        .attachments(List.of(
            BinaryContentDTO.builder().fileName("file1.png").build(),
            BinaryContentDTO.builder().fileName("file2.png").build()
        ))
        .build();

    given(messageService.createMessage(any(MessageCreateRequest.class), anyList()))
        .willReturn(dto);

    mockMvc.perform(multipart("/api/messages")
            .file(messagePart)
            .file(attachment1)
            .file(attachment2)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("Hello with Attachments"))
        .andExpect(jsonPath("$.attachments[0].fileName").value("file1.png"))
        .andExpect(jsonPath("$.attachments[1].fileName").value("file2.png"));

    verify(messageService).createMessage(any(MessageCreateRequest.class), anyList());
  }

}