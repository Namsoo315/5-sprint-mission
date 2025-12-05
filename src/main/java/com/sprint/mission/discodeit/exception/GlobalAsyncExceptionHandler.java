package com.sprint.mission.discodeit.exception;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

@Slf4j
public class GlobalAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

  @Override
  public void handleUncaughtException(Throwable ex, Method method, Object... params) {
    log.error("비동기 처리 중 예외 발생 - method={}, message={}",
        method.getName(), ex.getMessage(), ex);

    log.error("비동기 메서드 파라미터 값들:");
    for (Object param : params) {
      log.error("  > {}", param);
    }
  }
}