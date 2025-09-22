package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

    @Size(min = 4, max = 20, message = "사용자 아이디는 최소 4자에서 최대 20자까지 가능합니다.")
    String newUsername,

    @Email(message = "유효한 이메일 형식이어야 합니다.")
    String newEmail,

    @Size(min = 4, max = 20, message = "비밀번호는 최소 8자에서 최대 20자까지 가능합니다.")
//    @Pattern(
//        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$",
//        message = "비밀번호는 문자, 숫자, 특수문자를 모두 포함해야 합니다."
//    )
    String newPassword

) {

}
