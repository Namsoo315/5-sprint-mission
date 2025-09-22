package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 엔티티의 생성일 /수정일 같은 감사(Auditing)
public class DiscodeitApplication {

  public static void main(String[] args) {
    SpringApplication.run(DiscodeitApplication.class, args);
    System.out.println("http://localhost:8080/");
    System.out.println("http://localhost:8080/actuator");
  }
}
