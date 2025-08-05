package com.sprint.mission.discodeit.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttachmentDTO {
	private String fileName;
	private String contentType;
	private Long size;
	private byte[] binaryContent;
}
