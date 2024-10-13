package com.d129cm.backendapi.partners.manager;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.repository.PartnersRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PartnersManagerTest {

    @InjectMocks
    private PartnersManager partnersManager;

    @Mock
    private PartnersRepository partnersRepository;

    @Nested
    class updatePartnersBrand {

        @Test
        void 성공_파트너스_브랜드_업데이트() {
            // given
            Partners partners = mock(Partners.class);
            Brand brand = mock(Brand.class);

            // when
            partnersManager.updatePartnersBrand(partners, brand);

            // then
            verify(partnersRepository).save(partners);
        }
    }
}
