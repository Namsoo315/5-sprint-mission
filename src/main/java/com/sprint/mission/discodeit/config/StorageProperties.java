package com.sprint.mission.discodeit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "discodeit.storage")
public class StorageProperties {

  private String type;       // discodeit.storage.type
  private final Local local = new Local();

  @Getter
  @Setter
  public static class Local {

    private String rootPath;
  }
}
