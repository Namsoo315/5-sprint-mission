package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class BinaryContent {
	private final UUID BinaryContentId;
	private final UUID userId;
	private final byte[] binaryContent;
	private final Instant createdAt;
	// 수정 불가능 하기 때문에 updatedAt은 삭제

	public BinaryContent(UUID userId, byte[] binaryContent) {
		BinaryContentId = UUID.randomUUID();
		this.userId = userId;
		this.binaryContent = binaryContent;
		this.createdAt = Instant.now();
	}

	@Override
	public String toString() {
		return "BinaryContent{" +
			"BinaryContentId=" + BinaryContentId +
			", userId=" + userId +
			", binaryContent=" + Arrays.toString(binaryContent) +
			", createdAt=" + createdAt +
			'}';
	}
}
