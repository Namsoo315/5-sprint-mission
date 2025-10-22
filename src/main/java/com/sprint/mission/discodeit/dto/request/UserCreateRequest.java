package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserCreateRequest(
    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Size(min = 4, max = 20, message = "사용자 아이디는 최소 4자에서 최대 20자까지 가능합니다.")
    String username,

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    String email,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 20, message = "비밀번호는 최소 4자에서 최대 20자까지 가능합니다.")
//    @Pattern(
//        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$",
//        message = "비밀번호는 문자, 숫자, 특수문자를 모두 포함해야 합니다."
//    )
    String password
) {

}
