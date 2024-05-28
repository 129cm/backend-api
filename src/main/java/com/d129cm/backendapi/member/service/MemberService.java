package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.common.exception.BaseException;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.MemberSignupRequest;
import com.d129cm.backendapi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveMember(MemberSignupRequest request) {
        Password newPassword = Password.of(request.password(), passwordEncoder);
        if (memberRepository.existsByEmail(request.email())) {
            throw new BaseException("이미 가입된 이메일입니다.", HttpStatus.CONFLICT);
        }

        Member newMember = Member.builder()
                .email(request.email())
                .password(newPassword)
                .name(request.name())
                .address(request.address().toAddressEntity())
                .build();
        memberRepository.save(newMember);
    }
}
