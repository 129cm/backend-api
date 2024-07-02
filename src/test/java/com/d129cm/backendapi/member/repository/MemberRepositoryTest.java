package com.d129cm.backendapi.member.repository;

import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.config.InitializeTestContainers;
import com.d129cm.backendapi.member.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportTestcontainers(InitializeTestContainers.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Nested
    class create {
        @Test
        void 성공_멤버_저장() {
            //given
            Member member = Member.builder()
                    .email("test@naver.com")
                    .password(Mockito.mock(Password.class))
                    .name("이름")
                    .address(Mockito.mock(Address.class))
                    .build();

            // when
            Member savedMember = memberRepository.save(member);

            // then
            Assertions.assertAll(
                    () -> assertThat(savedMember).isNotNull(),
                    () -> assertThat(savedMember.getId()).isEqualTo(member.getId()),
                    () -> assertThat(savedMember.getEmail()).isEqualTo(member.getEmail()),
                    () -> assertThat(savedMember.getPassword()).isEqualTo(member.getPassword()),
                    () -> assertThat(savedMember.getName()).isEqualTo(member.getName()),
                    () -> assertThat(savedMember.getAddress()).isEqualTo(member.getAddress())
            );
        }
    }

    @Nested
    class find {
        @Test
        void 성공_멤버_조회() {
            // given
            Address address = new Address("1234", "Seoul", "Seoul");
            Member testMember = Member.builder()
                    .email("test@naver.com")
                    .password(Mockito.mock(Password.class))
                    .name("이름")
                    .address(address)
                    .build();
            testMember = memberRepository.save(testMember);
            Long memberId = testMember.getId();

            // when
            Optional<Member> member = memberRepository.findById(memberId);

            // then
            Assertions.assertAll(
                    () -> assertThat(member).isNotEmpty(),
                    () -> assertThat(member.get().getId()).isEqualTo(memberId),
                    () -> assertThat(member.get().getEmail()).isEqualTo("test@naver.com"),
                    () -> assertThat(member.get().getName()).isEqualTo("이름"),
                    () -> assertThat(member.get().getAddress().getZipCode()).isEqualTo(address.getZipCode()),
                    () -> assertThat(member.get().getAddress().getAddressDetails()).isEqualTo(address.getAddressDetails()),
                    () -> assertThat(member.get().getAddress().getRoadNameAddress()).isEqualTo(address.getRoadNameAddress())
            );
        }

        @Test
        void OptionalEmpty반환_멤버가_존재하지_않을_경우() {
            // given
            Long nonExistingId = Long.MAX_VALUE;

            // when
            Optional<Member> member = memberRepository.findById(nonExistingId);

            // then
            assertThat(member).isEmpty();
        }
    }
}
