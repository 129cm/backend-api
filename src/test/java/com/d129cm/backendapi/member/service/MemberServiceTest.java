package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.common.dto.AddressRequest;
import com.d129cm.backendapi.common.exception.BaseException;
import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.MemberSignupRequest;
import com.d129cm.backendapi.member.repository.MemberRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    class saveMember {
        @Test
        void 성공반환_멤버_저장() {
            // given
            AddressRequest addressRequest = new AddressRequest("1234", "Seoul", "Seoul");
            MemberSignupRequest request = new MemberSignupRequest(
                    "test@naver.com", "Asdf1234!", "이름", addressRequest);

            when(memberRepository.save(any(Member.class))).thenReturn(mock(Member.class));

            // when
            memberService.saveMember(request);

            // then
            verify(memberRepository).save(any(Member.class));
        }

        @Test
        void 예외반환_중복된_멤버() {
            // given
            AddressRequest addressRequest = new AddressRequest("1234", "Seoul", "Seoul");
            MemberSignupRequest request = new MemberSignupRequest(
                    "test@naver.com", "Asdf1234!", "이름", addressRequest);

            // when
            when(memberRepository.existsByEmail(request.email())).thenReturn(true);

            // then
            assertThrows(ConflictException.class, () -> memberService.saveMember(request));
        }
    }

}
