package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.exception.GlobalAsyncExceptionHandler;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.support.CompositeTaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

  private final MeterRegistry meterRegistry;

  @Bean(name = "taskExecutor")
  public ThreadPoolTaskExecutor taskExecutor() {
    int cores = Runtime.getRuntime().availableProcessors();
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(cores);
    taskExecutor.setMaxPoolSize(cores + 1);
    taskExecutor.setQueueCapacity(100);
    taskExecutor.setThreadNamePrefix("taskExecutor-");
    taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    taskExecutor.setTaskDecorator(new CompositeTaskDecorator(
        List.of(mdcTaskDecorator(), securityContextTaskDecorator())));

    taskExecutor.initialize();

    ExecutorServiceMetrics.monitor(meterRegistry
        , taskExecutor.getThreadPoolExecutor(),
        "taskExecutor-");

    return taskExecutor;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new GlobalAsyncExceptionHandler();
  }

  public TaskDecorator mdcTaskDecorator() {
    return task -> {
      Map<String, String> context = MDC.getCopyOfContextMap();
      return () -> {
        try {
          if (context != null) {
            MDC.setContextMap(context);
          }
          task.run();
        } finally {
          MDC.clear();
        }
      };
    };
  }

  public TaskDecorator securityContextTaskDecorator() {
    return task -> {
      SecurityContext context = SecurityContextHolder.getContext();
      return () -> {
        try {
          if (context != null) {
            SecurityContextHolder.setContext(context);
          }
          task.run();
        } finally {
          SecurityContextHolder.clearContext();
        }
      };
    };
  }
}
