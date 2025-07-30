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
	private final long serialVersionUID = 1L;
	private final UUID channelId;
	private String name;
	private String description;
	private final Instant createdAt;
	private Instant updatedAt;

	public Channel(String name, String description) {
		this.channelId = UUID.randomUUID();
		this.name = name;
		this.description = description;
		this.createdAt = Instant.now();
		this.updatedAt = createdAt;
	}

	public void update(String name, String description){
		this.name = name;
		this.description = description;
		this.updatedAt = Instant.now();
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Channel{");
		sb.append("channelId=").append(channelId);
		sb.append(", name='").append(name).append('\'');
		sb.append(", description='").append(description).append('\'');
		sb.append(", createdAt=").append(createdAt).append('\'');
		sb.append(", updatedAt=").append(updatedAt);
		sb.append('}');
		return sb.toString();
	}
}
