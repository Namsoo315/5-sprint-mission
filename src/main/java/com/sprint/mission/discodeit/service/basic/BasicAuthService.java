package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.JwtInformation;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.AuthLoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidUserCredentialsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtRegistry;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtRegistry<UUID> jwtRegistry;
  private final UserDetailsService userDetailsService;

  @Override
  public JwtInformation refreshToken(String refreshToken) {
    if (!jwtTokenProvider.validateRefreshToken(refreshToken)
        || !jwtRegistry.hasActiveJwtInformationByRefreshToken(refreshToken)) {
      log.info("Invalid or expired refresh token : {}", refreshToken);
      throw new RuntimeException("Invalid or expired refresh token");
    }

    String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    if (userDetails == null) {
      throw new UsernameNotFoundException("User not found");
    }

    try {
      DiscodeitUserDetails discodeitUserDetails = (DiscodeitUserDetails) userDetails;
      String newAccessToken = jwtTokenProvider.generateAccessToken(discodeitUserDetails);
      String newRefreshToken = jwtTokenProvider.generateRefreshToken(discodeitUserDetails);

      log.info("RefreshToken : {}", newRefreshToken);
      JwtInformation newJwtInformation = new JwtInformation(
          discodeitUserDetails.getUserDTO()
          , newAccessToken
          , newRefreshToken
      );

      jwtRegistry.rotateJwtInformation(refreshToken, newJwtInformation);
      return newJwtInformation;
    } catch (Exception e) {
      log.error("Failed to generate new tokens for user: {}", username, e);
      throw new RuntimeException(e);
    }
  }
}
