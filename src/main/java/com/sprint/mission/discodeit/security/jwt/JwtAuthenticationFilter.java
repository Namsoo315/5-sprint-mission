package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  // 헤더 검증 필터
  private final ObjectMapper objectMapper;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtRegistry<UUID> jwtRegistry;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getServletPath();
    return path.equals("/api/auth/refresh") || path.equals("/api/auth/login");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      String token = resolveToken(request);
      if (StringUtils.hasText(token)) {
        if (jwtTokenProvider.validateAccessToken(token) &&
            jwtRegistry.hasActiveJwtInformationByAccessToken(token)) {

          String username = jwtTokenProvider.getUsernameFromToken(token);

          UserDTO userDTO = jwtTokenProvider.parseAccessToken(token).userDTO();
          DiscodeitUserDetails discodeitUserDetails = new DiscodeitUserDetails(userDTO, null);
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
              discodeitUserDetails, null, discodeitUserDetails.getAuthorities());

          authenticationToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          log.debug("Set authentication for user: {}", username);
        } else {
          log.debug("Invalid JWT token");
          sendErrorResponse(response, "Invalid JWT token", HttpServletResponse.SC_UNAUTHORIZED);
          return;
        }
      }
    } catch (Exception e) {
      log.error("Authentication failed. {}", e.getMessage());
      SecurityContextHolder.clearContext();
      sendErrorResponse(response, "Authentication failed.", HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }
    filterChain.doFilter(request, response);
  }

  // Authorization: Bearer xxx 추출
  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  // 공통 에러 응답
  private void sendErrorResponse(HttpServletResponse response, String message, int status)
      throws IOException {
    ErrorResponse errorResponse = new ErrorResponse(new Exception(message), status);
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
