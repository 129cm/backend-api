package com.d129cm.backendapi.partners.repository;

import com.d129cm.backendapi.config.InitializeTestContainers;
import com.d129cm.backendapi.partners.domain.Partners;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportTestcontainers(InitializeTestContainers.class)
public class PartnersRepositoryTest {

    @Autowired
    private PartnersRepository partnersRepository;

    @Test
    void 성공_파트너_저장() {
        //given
        Partners partners = Partners.builder()
                .email("email@naver.com")
                .password("asdf1234!")
                .businessNumber("123-45-67890")
                .build();

        // when
        Partners savedPartners = partnersRepository.save(partners);

        // then
        Assertions.assertAll(
                () -> assertThat(savedPartners).isNotNull(),
                () -> assertThat(savedPartners.getId()).isEqualTo(partners.getId()),
                () -> assertThat(savedPartners.getEmail()).isEqualTo(partners.getEmail()),
                () -> assertThat(savedPartners.getPassword()).isEqualTo(partners.getPassword()),
                () -> assertThat(savedPartners.getBusinessNumber()).isEqualTo(partners.getBusinessNumber())
        );
    }
}
