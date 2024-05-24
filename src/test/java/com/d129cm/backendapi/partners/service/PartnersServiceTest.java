package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.common.exception.BaseException;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.PartnersSignupRequest;
import com.d129cm.backendapi.partners.repository.PartnersRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartnersServiceTest {

    @InjectMocks
    private PartnersService partnersService;

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

            // when
            partnersService.savePartners(request);

            // then
            verify(partnersRepository).save(any(Partners.class));
        }
        @Test
        void 예외반환_중복된_파트너스() {
            // given
            PartnersSignupRequest request = new PartnersSignupRequest("email@naver.com", "asdf1234!", "123-45-67890");

            // when
            when(partnersRepository.existsByEmail(request.email())).thenReturn(true);

            // then
            assertThrows(BaseException.class, () -> partnersService.savePartners(request));
        }
    }

}