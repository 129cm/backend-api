package com.d129cm.backendapi.auth.service;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public MemberDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found with email: " + memberEmail));
        return new MemberDetails(member);

    }
}
