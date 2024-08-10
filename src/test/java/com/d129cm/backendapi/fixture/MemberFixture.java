package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.member.domain.Member;

import static org.mockito.Mockito.spy;

public class MemberFixture {

    public static Member createMember(String email) {
        Password password = spy(PasswordFixture.createPassword());
        Address address = AddressFixture.createAddress();
        Member member = spy(
                Member.builder()
                        .email(email)
                        .password(password)
                        .name("이름")
                        .address(address)
                        .build());
        return member;
    }
}
