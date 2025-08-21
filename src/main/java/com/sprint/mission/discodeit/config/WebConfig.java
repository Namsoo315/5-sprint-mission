package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")    //API 경로에 대해서만
        .allowedOriginPatterns("http://localhost:8080")
        .allowedMethods("GET", "POST", "PATCH", "DELETE") //PUT은 사용안해서 일단 뺌
        .allowedHeaders("*")              // 필요 헤더인데 뭐가 필요한지 좀 더 고민.
        .allowCredentials(true)
        .maxAge(3600);
  }
}
