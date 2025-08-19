package com.sprint.mission.discodeit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "discodeit.repository")
public class RepositoryProperties {
	private String type;
	private String fileDirectory;
	private String extension;
}
