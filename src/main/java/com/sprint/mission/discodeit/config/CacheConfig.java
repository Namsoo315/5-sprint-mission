package com.sprint.mission.discodeit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager manager = new CaffeineCacheManager("users", "channels", "notifications");

    // 세팅
    manager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(10_000)
//        .expireAfterWrite(10, TimeUnit.MINUTES)
            .expireAfterWrite(
                Duration.ofMinutes(10 + ThreadLocalRandom.current().nextInt(-2, 3)))  // +- 2m 랜덤 시간 추가
            .expireAfterAccess(Duration.ofMinutes(5))
            .recordStats()
            .removalListener(((key, value, cause) -> {
              log.info("removalListener key ={}, value ={}, cause ={}", key, value, cause);
            }))
            .evictionListener((key, value, cause) -> {
              log.info("evictionListener key ={}, value ={}, cause ={}", key, value, cause);
            })
    );

    return manager;
  }
}
