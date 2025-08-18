package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ApiResponse<T> {
    private boolean success;    // 응답의 성공 여부
    private String message;    // 응답 메시지
    private T data;            // 실제 body 영역
    private Instant timestamp;  // Rest 응답 시간

    // 공통 API를 생성하기 위한 생성 메서드

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> ok( T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(Instant.now())
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> ok(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .timestamp(Instant.now())
                .message(message)
                .build();
    }
}
