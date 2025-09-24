package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ChannelController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private ChannelService channelService;

  @MockitoBean
  private JpaMetamodelMappingContext jpaMetamodelMappingContext; // 가상의 jpa을 실행해줄 환경

  @Test
  @DisplayName("공개 채널 생성 테스트")
  void createPublicChannelTest() throws Exception {
    PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(
        "publicChannel", "공개 채널");
    String request = objectMapper.writeValueAsString(publicChannelCreateRequest);

    ChannelDTO channelDTO = objectMapper.readValue(request, ChannelDTO.class);

    given(channelService.createPublicChannel(any())).willReturn(channelDTO);

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("publicChannel"))
        .andExpect(jsonPath("$.description").value("공개 채널"));
  }

  @Test
  @DisplayName("비공개 채널 생성 테스트")
  void createPrivateChannelTest() throws Exception {
    UUID user1Id = UUID.randomUUID();
    UUID user2Id = UUID.randomUUID();

    PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(
        List.of(user1Id, user2Id));
    String request = objectMapper.writeValueAsString(privateChannelCreateRequest);
    ChannelDTO channelDTO = ChannelDTO.builder()
        .type(ChannelType.PRIVATE)
        .participants(
            List.of(UserDTO.builder().id(user1Id).build(), UserDTO.builder().id(user2Id).build()))
        .build();

    given(channelService.createPrivateChannel(any())).willReturn(channelDTO);

    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.participants").isNotEmpty());
  }

  @Test
  @DisplayName("비공개 채널 생성 실패 테스트 (2명 이상 생성 가능)")
  void createPrivateChannelFailTest() throws Exception {
    UUID user1Id = UUID.randomUUID();
    PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(
        List.of(user1Id));
    String request = objectMapper.writeValueAsString(privateChannelCreateRequest);

    ChannelDTO channelDTO = ChannelDTO.builder()
        .type(ChannelType.PRIVATE)
        .participants(
            List.of(UserDTO.builder().id(user1Id).build()))
        .build();

    given(channelService.createPrivateChannel(any())).willReturn(channelDTO);

    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("공개 채널 수정 테스트")
  void updatePublicChannelTest() throws Exception {
    PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(
        "publicChannel", "공개 채널");

    ChannelDTO createDTO = ChannelDTO.builder()
        .id(UUID.randomUUID())
        .type(ChannelType.PUBLIC)
        .name(publicChannelCreateRequest.name())
        .description(publicChannelCreateRequest.description())
        .build();

    given(channelService.createPublicChannel(any())).willReturn(createDTO);

    ChannelUpdateRequest updateRequest = new ChannelUpdateRequest("updatedName", "updatedDesc");
    String request = objectMapper.writeValueAsString(updateRequest);

    ChannelDTO updatedDTO = ChannelDTO.builder()
        .id(createDTO.id())
        .type(createDTO.type())
        .name(updateRequest.newName())
        .description(updateRequest.newDescription())
        .build();

    given(channelService.updateChannel(eq(updatedDTO.id()), any(ChannelUpdateRequest.class)))
        .willReturn(updatedDTO);

    mockMvc.perform(patch("/api/channels/" + updatedDTO.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("updatedName"))
        .andExpect(jsonPath("$.description").value("updatedDesc"));
  }


  @Test
  @DisplayName("채널 삭제 테스트")
  void deleteChannelTest() throws Exception {
    PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(
        "publicChannel", "공개 채널");

    ChannelDTO createDTO = ChannelDTO.builder()
        .id(UUID.randomUUID())
        .type(ChannelType.PUBLIC)
        .name(publicChannelCreateRequest.name())
        .description(publicChannelCreateRequest.description())
        .build();

    given(channelService.createPublicChannel(any())).willReturn(createDTO);

    mockMvc.perform(delete("/api/channels/" + createDTO.id()))
        .andExpect(status().isNoContent());
  }
}
