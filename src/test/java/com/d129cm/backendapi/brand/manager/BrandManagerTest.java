package com.d129cm.backendapi.brand.manager;

import com.d129cm.backendapi.brand.repository.BrandRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class BrandManagerTest {

    @InjectMocks
    private BrandManager brandManager;

    @Mock
    private BrandRepository brandRepository;

    @Nested
    class existByBrandName{

        @Test
        void true반환_브랜드_존재_확인(){
            // given
            when(brandRepository.existsByName("브랜드이름")).thenReturn(true);

            // when
            // then
            assertThat(brandManager.existByBrandName("브랜드이름")).isTrue();
        }
    }
}
