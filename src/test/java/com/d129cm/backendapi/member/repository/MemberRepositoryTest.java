package com.d129cm.backendapi.member.repository;

import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.config.InitializeTestContainers;
import com.d129cm.backendapi.member.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportTestcontainers(InitializeTestContainers.class)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

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
