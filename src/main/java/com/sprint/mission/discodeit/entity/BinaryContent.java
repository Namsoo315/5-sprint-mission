package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private final UUID BinaryContentId;
	private final Instant createdAt;
	private String fileName;
	private String contentType;
	private Long size;
	private final byte[] binaryContent;
	// 수정 불가능 하기 때문에 updatedAt은 삭제

	public BinaryContent(String fileName, String contentType, Long size, byte[] binaryContent) {
		this.BinaryContentId = UUID.randomUUID();
		this.createdAt = Instant.now();
		this.fileName = fileName;
		this.contentType = contentType;
		this.size = size;
		this.binaryContent = binaryContent;
	}
}
