package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.common.dto.AddressResponse;
import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.MemberMyPageResponse;
import com.d129cm.backendapi.member.dto.MemberSignupRequest;
import com.d129cm.backendapi.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
            throw new ConflictException("email", request.email());
        }

        Member newMember = Member.builder()
                .email(request.email())
                .password(newPassword)
                .name(request.name())
                .address(request.address().toAddressEntity())
                .build();
        memberRepository.save(newMember);
    }

    public MemberMyPageResponse getMemberMyPage(MemberDetails memberDetails) {
        Member member = memberRepository.findById(memberDetails.member().getId())
                .orElseThrow(()-> new EntityNotFoundException("일치하는 멤버가 없습니다."));
        AddressResponse addressResponse = AddressResponse.of(member.getAddress());
        return new MemberMyPageResponse(member.getEmail(), member.getName(), addressResponse);
    }
}
