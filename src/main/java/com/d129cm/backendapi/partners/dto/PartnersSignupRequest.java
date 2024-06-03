package com.d129cm.backendapi.partners.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PartnersSignupRequest(
        @Email(message = "이메일 형식이 아닙니다.")
        @NotNull(message = "이메일은 필수 입력 값입니다.")
        String email,
        @NotNull(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$", message = "비밀번호는 영문 대소문자, 숫자를 포함해야 합니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상, 20자 이하여야 합니다.")
        String password,
        @NotNull(message = "사업자 등록 번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(1\\d{2}|[2-9]\\d{2})-\\d{2}-\\d{5}$", message = "사업자 등록 번호 형식이 올바르지 않습니다.")
        String businessNumber) {
}
