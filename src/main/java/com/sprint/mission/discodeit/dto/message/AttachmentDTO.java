package com.sprint.mission.discodeit.dto.message;

import java.util.Arrays;

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

	@Override
	public String toString() {
		return "AttachmentDTO{" +
			"fileName='" + fileName + '\'' +
			", contentType='" + contentType + '\'' +
			", size=" + size +
			", binaryContent=" + Arrays.toString(binaryContent) +
			'}';
	}
}
