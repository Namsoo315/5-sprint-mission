package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserCreateRequest {
	private String username;
	private String email;
	private String password;
	private String fileName;
	private String contentType;
	private Long size;
	private byte[] binaryContent;
}
