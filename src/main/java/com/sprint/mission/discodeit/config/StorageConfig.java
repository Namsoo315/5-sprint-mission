package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.type.LocalBinaryContentStorage;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfig {

  @Bean
  @ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "local")
  public BinaryContentStorage localBinaryContentStorage(StorageProperties props) {
    Path root = Paths.get(props.getLocal().getRootPath())
        .toAbsolutePath().normalize();
    return new LocalBinaryContentStorage(root);
  }
}
