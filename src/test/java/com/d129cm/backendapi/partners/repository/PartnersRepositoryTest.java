package com.d129cm.backendapi.partners.repository;

import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.config.InitializeTestContainers;
import com.d129cm.backendapi.partners.domain.Partners;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportTestcontainers(InitializeTestContainers.class)
public class PartnersRepositoryTest {

    @Autowired
    private PartnersRepository partnersRepository;

    @BeforeEach
    void setUp() {
        partnersRepository.deleteAll();

        Partners partners = Partners.builder()
                .email("test@naver.com")
                .password(mock(Password.class))
                .businessNumber("111-11-11111")
                .build();

        partnersRepository.save(partners);
    }

    @Test
    void 성공_파트너_저장() {
        //given
        Partners partners = Partners.builder()
                .email("email@naver.com")
                .password(mock(Password.class))
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

    @Nested
    class existByEmail {
        @Test
        void true_반환_파트너스_존재유무_확인() {
            //given
            Partners existPartners = Partners.builder()
                    .email("test@naver.com")
                    .password(mock(Password.class))
                    .businessNumber("111-11-11111")
                    .build();

            // when
            boolean result = partnersRepository.existsByEmail(existPartners.getEmail());

            // then
            assertThat(result).isTrue();
        }
    }

    @Nested
    class findByEmail {
        @Test
        void 유효한_파트너스_반환_email로_파트너스_찾기() {
            // given
            Partners existPartners = Partners.builder()
                    .email("test@naver.com")
                    .password(mock(Password.class))
                    .businessNumber("111-11-11111")
                    .build();

            // when
            Optional<Partners> partners = partnersRepository.findByEmail(existPartners.getEmail());

            // then
            assertThat(partners).isPresent();
            assertThat(partners.get().getEmail()).isEqualTo(existPartners.getEmail());
        }
    }
}
