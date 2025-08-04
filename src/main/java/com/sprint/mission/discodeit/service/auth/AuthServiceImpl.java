package com.sprint.mission.discodeit.service.auth;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.auth.AuthLoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service("authService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

	@Override
	public AuthLoginResponse login(AuthLoginRequest request) {
		Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());

		if(optionalUser.isEmpty()) {
			throw new IllegalArgumentException("존재하지 않는 회원입니다.");
		}
		User user = optionalUser.get();

		if(!user.getPassword().equals(request.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		return AuthLoginResponse.builder().user(user).build();
	}
}
