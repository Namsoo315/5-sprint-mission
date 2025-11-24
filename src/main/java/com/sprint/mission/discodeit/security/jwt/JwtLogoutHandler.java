package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtRegistry<UUID> jwtRegistry;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    Cookie refreshTokenExpirationCookie = jwtTokenProvider.genereateRefreshTokenExpirationCookie();
    response.addCookie(refreshTokenExpirationCookie);
    try {
      jwtRegistry.invalidateJwtInformationByUserId(
          ((DiscodeitUserDetails) authentication.getPrincipal()).getUserDTO().id());
    } catch (Exception e) {
      log.debug("JWT logout handler executed - refresh token cookie cleared");
    }
  }
}
