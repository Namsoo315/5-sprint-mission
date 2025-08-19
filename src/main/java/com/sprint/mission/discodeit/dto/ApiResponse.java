package com.sprint.mission.discodeit.dto;

import java.time.Instant;

public record ApiResponse<T>(
	boolean success,
	String message,
	T data,
	Instant timestamp
) {
	public static <T> ApiResponse<T> ok(T data) {
		return new ApiResponse<>(true, null, data, Instant.now());
	}

	public static <T> ApiResponse<T> ok(T data, String message) {
		return new ApiResponse<>(true, message, data, Instant.now());
	}

	public static <T> ApiResponse<T> ok(String message) {
		return new ApiResponse<>(true, message, null, Instant.now());
	}

	public static <T> ApiResponse<T> fail(String message) {
		return new ApiResponse<>(false, message, null, Instant.now());
	}
}
