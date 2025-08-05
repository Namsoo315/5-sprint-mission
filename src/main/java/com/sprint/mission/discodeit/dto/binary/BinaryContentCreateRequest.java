package com.sprint.mission.discodeit.dto.binary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BinaryContentCreateRequest {
	private String fileName;
	private String contentType;
	private Long size;
	private byte[] binaryContent;
}
