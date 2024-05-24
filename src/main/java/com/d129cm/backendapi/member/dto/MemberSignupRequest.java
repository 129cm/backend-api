package com.d129cm.backendapi.member.dto;

import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.member.domain.Member;

public record MemberSignupRequest(
        String email,
        String password,
        String name,
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

