package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new io.swagger.v3.oas.models.info.Info()
            .title("API 문서")
            .version("v1.0.0")
            .description("Swagger API 문서입니다."))
        .servers(java.util.List.of(
            new io.swagger.v3.oas.models.servers.Server()
                .url("http://localhost:8080")
                .description("로컬 서버"),
            new io.swagger.v3.oas.models.servers.Server()
                .url("http://127.0.0.1:8080")
                .description("로컬 서버2")
        ));
  }

}