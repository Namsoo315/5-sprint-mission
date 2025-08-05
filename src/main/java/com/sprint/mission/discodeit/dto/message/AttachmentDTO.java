package com.sprint.mission.discodeit.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AttachmentDTO {
	private String fileName;
	private String contentType;
	private Long size;
	private byte[] binaryContent;
}
