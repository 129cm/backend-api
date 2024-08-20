package com.d129cm.backendapi.member.manager;

import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberManager {

    private final MemberRepository memberRepository;

    public Member getMember (String email) {
        return memberRepository.findByEmail(email).orElseThrow(NotFoundException::entityNotFound);
    }
}
