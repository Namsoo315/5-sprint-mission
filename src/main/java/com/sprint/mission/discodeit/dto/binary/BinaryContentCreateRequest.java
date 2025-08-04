package com.sprint.mission.discodeit.dto.binary;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BinaryContentCreateRequest {
	private UUID BinaryContentId;
	private List<UUID> attachmentIds;
	private String fileName;
	private String contentType;
	private Long size;
	private byte[] binaryContent;
}
