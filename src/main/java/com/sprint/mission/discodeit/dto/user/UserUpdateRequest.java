package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserUpdateRequest {
	private UUID userId;
	private String username;
	private String email;
	private String password;
	private String fileName;
	private String contentType;
	private Long size;
	private byte[] binaryContent;
}
