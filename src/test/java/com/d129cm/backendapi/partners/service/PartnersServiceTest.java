package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.PartnersSignupRequest;
import com.d129cm.backendapi.partners.repository.PartnersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class PartnersServiceTest {

    @InjectMocks
    private PartnersService partnersService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PartnersRepository partnersRepository;

    @Nested
    class savePartners {
        @Test
        void 성공_파트너스_저장() {
            // given
            PartnersSignupRequest request = new PartnersSignupRequest(
                    "email@naver.com", "asdf1234!", "123-45-67890");

            when(partnersRepository.save(any(Partners.class))).thenReturn(mock(Partners.class));
            when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");

            // when
            partnersService.savePartners(request);

            // then
            verify(partnersRepository).save(any(Partners.class));
            verify(passwordEncoder).encode(request.password());
        }

        @Test
        void 예외반환_중복된_파트너스() {
            // given
            PartnersSignupRequest request = new PartnersSignupRequest("email@naver.com", "asdf1234!", "123-45-67890");
            ConflictException e = new ConflictException("email", request.email());

            // when
            when(partnersRepository.existsByEmail(request.email())).thenReturn(true);

            // then
            Assertions.assertThatThrownBy(() -> partnersService.savePartners(request))
                    .isInstanceOf(e.getClass()).hasMessage(e.getMessage());
        }
    }

}