package com.d129cm.backendapi.brand.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.dto.BrandCreateRequest;
import com.d129cm.backendapi.brand.manager.BrandManager;
import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.manager.PartnersManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class BrandServiceTest {

    @InjectMocks
    private BrandService brandService;

    @Mock
    private BrandManager brandManager;

    @Mock
    private PartnersManager partnersManager;

    @Nested
    class createBrand {

        @Test
        void 성공_브랜드_생성() {
            // given
            BrandCreateRequest request = spy(new BrandCreateRequest("brandName", "brandDescription", "brandImage"));
            Partners mockPartners = mock(Partners.class);
            Brand mockBrand = request.toBrandEntity();
            when(request.toBrandEntity()).thenReturn(mockBrand);

            // when
            brandService.createBrand(mockPartners, request);

            // then
            verify(partnersManager).updatePartnersBrand(eq(mockPartners), eq(mockBrand));
        }

        @Test
        void 실패400_브랜드를_갖고있는_파트너스_브랜드_추가() {
            // given
            BrandCreateRequest request = spy(new BrandCreateRequest("brandName", "brandDescription", "brandImage"));
            Partners mockPartners = mock(Partners.class);
            Brand mockBrand = request.toBrandEntity();
            when(mockPartners.getBrand()).thenReturn(mockBrand);

            // when & then
            BadRequestException e = BadRequestException.relationAlreadyExist("Brand");
            Assertions.assertThatThrownBy(() -> brandService.createBrand(mockPartners, request))
                    .isInstanceOf(e.getClass()).hasMessage(e.getMessage());
        }

        @Test
        void 실패409_이미_존재하는_브랜드_이름() {
            // given
            BrandCreateRequest request = spy(new BrandCreateRequest("brandName", "brandDescription", "brandImage"));
            Partners mockPartners = mock(Partners.class);
            Brand mockBrand = request.toBrandEntity();
            when(brandManager.existByBrandName(mockBrand.getName())).thenReturn(true);

            // when & then
            ConflictException e = ConflictException.duplicatedValue("Brand", mockBrand.getName());
            Assertions.assertThatThrownBy(() -> brandService.createBrand(mockPartners, request))
                    .isInstanceOf(e.getClass()).hasMessage(e.getMessage());
        }
    }
}
