package com.d129cm.backendapi.member.dto;

import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberSignupRequest(
        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상, 20자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$", message = "비밀번호는 영문 대소문자, 숫자를 포함해야 합니다.")
        String password,

        @NotBlank(message = "이름은 필수 입력 값입니다.")
        String name,

        @NotBlank(message = "주소는 필수 입력 값입니다.")
        Address address) {

    public Member toMemberEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .address(address)
                .build();
    }
}

