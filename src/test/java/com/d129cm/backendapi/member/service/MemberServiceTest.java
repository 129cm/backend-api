package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.exception.BaseException;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.MemberSignupRequest;
import com.d129cm.backendapi.member.repository.MemberRepository;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.PartnersSignupRequest;
import com.d129cm.backendapi.partners.repository.PartnersRepository;
import com.d129cm.backendapi.partners.service.PartnersService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    class saveMember {
        @Test
        void 성공반환_멤버_저장() {
            // given
            MemberSignupRequest request = new MemberSignupRequest(
                    "test@naver.com", "asdf1234!", "이름", mock(Address.class)
            );

            when(memberRepository.save(any(Member.class))).thenReturn(mock(Member.class));

            // when
            memberService.saveMember(request);

            // then
            verify(memberRepository).save(any(Member.class));
        }

        @Test
        void 예외반환_중복된_멤버() {
            // given
            MemberSignupRequest request = new MemberSignupRequest(
                    "test@naver.com", "Asdf1234!", "이름", mock(Address.class)
            );

            // when
            when(memberRepository.existsByEmail(request.email())).thenReturn(true);

            // then
            assertThrows(BaseException.class, () -> memberService.saveMember(request));
        }
    }

}
