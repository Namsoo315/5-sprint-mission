package com.sprint.mission.discodeit.service.basic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;

import lombok.RequiredArgsConstructor;

@Service("userStatusService")
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
	private final UserStatusRepository userStatusRepository;
	private final UserRepository userRepository;

	@Override
	public UserStatus createUserStatus(UserStatusCreateRequest request) {
		if(userRepository.findById(request.getUserId()).isEmpty()){
			throw new IllegalArgumentException("존재하지 않는 유저입니다.");
		}

		if(userStatusRepository.findByUserId(request.getUserId()).isPresent()){
			throw new IllegalArgumentException("이미 존재하는 유저입니다.");
		}

		UserStatus userStatus = new UserStatus(request.getUserId());
		userStatusRepository.save(userStatus);

		return userStatus;
	}

	@Override
	public Optional<UserStatus> findById(UUID userStatusId) {
		return userStatusRepository.findById(userStatusId);
	}

	@Override
	public List<UserStatus> findAll() {
		return userStatusRepository.findAll();
	}

	@Override
	public UserStatus updateUserStatus(UserStatusUpdateRequest request) {
		UserStatus userStatus = userStatusRepository.findById(request.getUserStatusId()).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 유저 상태 정보입니다."));

		userStatus.updateStatus();

		return userStatus;
	}

	@Override
	public UserStatus updateByUserId(UUID userId) {
		UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));

		userStatus.updateStatus();

		return userStatus;
	}

	@Override
	public void delete(UUID userStatusId) {
		userStatusRepository.delete(userStatusId);
	}
}
