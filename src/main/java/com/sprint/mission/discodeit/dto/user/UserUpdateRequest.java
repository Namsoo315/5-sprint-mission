package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserUpdateRequest (
	UUID userId,
	String username,
	String email,
	String password
) {}
