package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.JwtInformation;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.AuthLoginRequest;

public interface AuthService {

  UserDTO login(AuthLoginRequest request);

  JwtInformation refreshToken(String refreshToken);
}
