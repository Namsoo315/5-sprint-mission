package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Channel implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private final UUID channelId;

	private String name;
	private String description;
	private final ChannelType type;

	private final Instant createdAt;
	private Instant updatedAt;

	public Channel(ChannelType type, String name, String description) {
		this.channelId = UUID.randomUUID();
		this.type = type;
		this.name = name;
		this.description = description;
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
	}

	public void update(String name, String description) {
		this.name = name;
		this.description = description;
		this.updatedAt = Instant.now();
	}

	@Override
	public String toString() {
		return "Channel{" +
			"channelId=" + channelId +
			", name='" + name + '\'' +
			", description='" + description + '\'' +
			", type=" + type +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			'}';
	}
}
