package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.dto.BrandResponse;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.PartnersMyPageResponse;
import com.d129cm.backendapi.partners.dto.PartnersSignupRequest;
import com.d129cm.backendapi.partners.repository.PartnersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
            PartnersSignupRequest request = new PartnersSignupRequest("test@example.com", "password123", "123456789");

            when(partnersRepository.existsByEmail(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

            // when
            partnersService.savePartners(request);

            // then
            ArgumentCaptor<Partners> partnersCaptor = ArgumentCaptor.forClass(Partners.class);
            verify(partnersRepository).save(partnersCaptor.capture());

            Partners savedPartner = partnersCaptor.getValue();
            assertEquals("encodedPassword", savedPartner.getPassword().getPassword());
        }

        @Test
        void 예외반환_중복된_파트너스() {
            // given
            PartnersSignupRequest request = new PartnersSignupRequest("email@naver.com", "asdf1234!", "123-45-67890");
            ConflictException e = ConflictException.duplicatedValue("email", request.email());

            // when
            when(partnersRepository.existsByEmail(request.email())).thenReturn(true);

            // then
            Assertions.assertThatThrownBy(() -> partnersService.savePartners(request))
                    .isInstanceOf(e.getClass()).hasMessage(e.getMessage());
        }
    }

    @Nested
    class getPartnersMyPage {
        @Test
        void 성공_파트너스_마이페이지_조회() {
            // given
            Brand brand = mock(Brand.class);
            Partners partners = Partners.builder()
                    .email("test@test.com")
                    .password(mock(Password.class))
                    .businessNumber("123-45-67890")
                    .build();
            partners.setBrand(brand);

            // when
            PartnersMyPageResponse response = partnersService.getPartnersMyPage(partners);

            // then
            Assertions.assertThat(response.brand()).isEqualTo(BrandResponse.of(brand));
        }

        @Test
        void 성공_파트너스_마이페이지_조회_브랜드없음() {
            // given
            Partners partners = Partners.builder()
                    .email("test@naver.com")
                    .password(mock(Password.class))
                    .businessNumber("123-45-67890")
                    .build();

            // when
            PartnersMyPageResponse response = partnersService.getPartnersMyPage(partners);

            // then
            Assertions.assertThat(response.brand()).isNull();
        }
    }
}