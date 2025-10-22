package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthLoginRequest(
    @NotBlank
    @Size(min = 4, max = 20, message = "사용자 아이디는 최소 4자에서 최대 20자까지 가능합니다.")
    String username,

    @NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
    @Size(min = 4, max = 20, message = "비밀번호는 최소 4자에서 최대 20자까지 가능합니다.")
//    @Pattern(
//        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$",
//        message = "비밀번호는 문자, 숫자, 특수문자를 모두 포함해야 합니다."
//    )
    String password
) {

}