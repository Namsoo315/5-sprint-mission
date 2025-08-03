package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {

	@Serial
	private final long serialVersionUID = 1L;
	private final UUID channelId;
	private String name;
	private String description;
	private final Long createdAt;
	private Long updatedAt;

	public Channel(String name, String description) {
		this.channelId = UUID.randomUUID();
		this.name = name;
		this.description = description;
		this.createdAt = System.currentTimeMillis();
		this.updatedAt = createdAt;
	}
	public UUID getChannelId() {
		return channelId;
	}

	public String getName() {
		return name;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void update(String name, String description){
		this.name = name;
		this.description = description;
		this.updatedAt = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final StringBuffer sb = new StringBuffer("Channel{");
		sb.append("channelId=").append(channelId);
		sb.append(", name='").append(name).append('\'');
		sb.append(", description='").append(description).append('\'');
		sb.append(", createdAt=").append(dateFormat.format(createdAt));
		sb.append(", updatedAt=").append(dateFormat.format(updatedAt));
		sb.append('}');
		return sb.toString();
	}
}
