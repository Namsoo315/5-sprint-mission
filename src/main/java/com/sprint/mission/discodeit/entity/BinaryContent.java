package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private final UUID BinaryContentId;

	private final UUID profileId;    // UserId
	private final List<UUID> attachmentIds;    // Message attachmentId -> 첨부파일 Id?

	private final Instant createdAt;

	private final String fileName;
	private final String contentType;
	private final Long size;
	private final byte[] binaryContent;
	// 수정 불가능 하기 때문에 updatedAt은 삭제

	public BinaryContent(UUID profileId, List<UUID> attachmentIds, String fileName, String contentType, Long size, byte[] binaryContent) {
		this.BinaryContentId = UUID.randomUUID();
		this.profileId = profileId;
		this.attachmentIds = attachmentIds;
		this.createdAt = Instant.now();
		this.fileName = fileName;
		this.contentType = contentType;
		this.size = size;
		this.binaryContent = binaryContent;
	}
}
