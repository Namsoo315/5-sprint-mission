package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.sprint.mission.discodeit.dto.data.JwtDTO;
import com.sprint.mission.discodeit.dto.data.JwtInformation;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtRegistry<UUID> jwtRegistry;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    if (authentication.getPrincipal() instanceof DiscodeitUserDetails discodeitUserDetails) {
      try {
        String accessToken = jwtTokenProvider.generateAccessToken(discodeitUserDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(discodeitUserDetails);

        Cookie refreshTokenCookie = jwtTokenProvider.generateRefreshTokenCookie(refreshToken);
        response.addCookie(refreshTokenCookie);

        JwtDTO jwtDTO = new JwtDTO(discodeitUserDetails.getUserDTO(), accessToken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(jwtDTO));

        log.info("jwtDTO : {}", objectMapper.writeValueAsString(jwtDTO));
        jwtRegistry.registerJwtInformation(
            new JwtInformation(discodeitUserDetails.getUserDTO(), accessToken, refreshToken));

        log.info("Successfully registered JWT for user {}", discodeitUserDetails.getUserDTO());
      } catch (JOSEException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e,
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
      }
    } else {
      log.error("UNAUTHORIZED Error!!");
      ErrorResponse errorResponse = new ErrorResponse(new Exception("Authorization failure!"),
          HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
  }
}
