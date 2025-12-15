package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOriginPatterns("http://localhost:3000")
        .allowedMethods("GET", "POST", "PATCH", "DELETE")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);

    registry.addMapping("/ws/**")
        .allowedOriginPatterns("http://localhost:3000")
        .allowedMethods("GET", "POST", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);

  }
}
