package com.coding_bottle.Calendar.domain.member.dto;

import javax.validation.constraints.*;

public record MemberPostDto(
        @NotNull(message = "이메일을 작성해주세요.")
        @NotBlank(message = "이메일은 공백일 수 없습니다.")
        @Email(message = "올바른 이메일 주소를 입력해주세요.")
        String email,

        //최소 8자 이상의 비밀번호.
        //적어도 하나의 알파벳(대소문자), 하나의 숫자, 하나의 특수 문자를 포함해야 함.
        @NotNull(message = "비밀번호를 작성해주세요.")
        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "비밀번호 양식에 부합하지 않습니다.")
        String password,

        @NotNull(message = "닉네임을 작성해주세요.")
        @NotBlank(message = "닉네임은 공백일 수 없습니다.")
        @Size(min = 1, max = 20, message = "닉네임은 1자 이상 20자 이하여야 합니다.")
        String nickname
) {
}