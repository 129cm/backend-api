package com.d129cm.backendapi.brand.manager;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.repository.BrandRepository;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.partners.domain.Partners;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class BrandManagerTest {

    @InjectMocks
    private BrandManager brandManager;

    @Mock
    private BrandRepository brandRepository;

    @Nested
    class getBrand {

        @Test
        void Brand반환_브랜드_조회() {
            // given
            Long brandId = 1L;
            Brand brand = Brand.builder()
                    .name("브랜드 이름")
                    .description("브랜드 설명")
                    .image("브랜드 대표 이미지")
                    .partners(mock(Partners.class))
                    .build();
            when(brandRepository.findById(brandId)).thenReturn(Optional.ofNullable(brand));

            // when
            Brand result = brandManager.getBrand(brandId);

            // then
            assertThat(result).isEqualTo(brand);
        }

        @Test
        void 에러반환_브랜드_조회() {
            // given
            Long brandId = 1L;
            when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

            // when
            // then
            assertThrows(EntityNotFoundException.class, () -> brandManager.getBrand(brandId));
        }
    }

    @Nested
    class existByBrandName {

        @Test
        void true반환_브랜드_존재_확인() {
            // given
            when(brandRepository.existsByName("브랜드이름")).thenReturn(true);

            // when
            // then
            assertThat(brandManager.existByBrandName("브랜드이름")).isTrue();
        }
    }

    @Nested
    class updateBrandItem {

        @Test
        void 성공_브랜드_아이템_추가() {
            // given
            Item item = mock(Item.class);
            Brand brand = mock(Brand.class);

            // when
            brandManager.updateBrandItem(brand, item);

            // then
            verify(brand).addItem(item);
            verify(brandRepository).save(brand);

        }
    }

    @Nested
    class getBrandWithItems {

        @Test
        void 성공_브랜드_반환() {
            // given
            Partners partners = mock(Partners.class);
            Brand brand = mock(Brand.class);
            when(brandRepository.findByPartners(partners)).thenReturn(Optional.of(brand));

            // when
            Brand result = brandManager.getBrandWithItems(partners);

            // then
            verify(brandRepository, times(1)).findByPartners(partners);
            assertThat(result).isEqualTo(brand);
        }

        @Test
        void 실패_파트너스의_브랜드가_없다() {
            // given
            Partners partners = mock(Partners.class);
            when(brandRepository.findByPartners(partners)).thenReturn(Optional.empty());

            // when & then
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> brandManager.getBrandWithItems(partners));

            verify(brandRepository, times(1)).findByPartners(partners);
            assertThat(exception.getMessage()).isEqualTo("일치하는 브랜드가 없습니다.");
        }

    }
}
