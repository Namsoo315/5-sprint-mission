package com.sprint.mission.discodeit.dto.binary;


public record BinaryContentDTO(
	String fileName,
	String contentType,
	Long size,
	byte[] binaryContent){}

