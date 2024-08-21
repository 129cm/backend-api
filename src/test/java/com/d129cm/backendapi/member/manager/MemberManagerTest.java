package com.d129cm.backendapi.member.manager;

import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.fixture.MemberFixture;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.repository.MemberRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberManagerTest {

    @InjectMocks
    private MemberManager memberManager;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    class getMember {
        @Test
        void Member반환_멤버_조회() {
            // given
            String email = "test@exmaple.com";
            Member member = MemberFixture.createMember(email);
            when(memberRepository.findByEmail(email)).thenReturn(Optional.ofNullable(member));

            // when
            Member result = memberManager.getMember(email);

            // then
            assertThat(result).isEqualTo(member);
        }

        @Test
        void 에러반환_멤버가_존재하지_않는_경우() {
            // given
            String email = "test@exmaple.com";
            when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

            // when
            // then
            assertThrows(NotFoundException.class, () -> memberManager.getMember(email));
        }
    }
}
