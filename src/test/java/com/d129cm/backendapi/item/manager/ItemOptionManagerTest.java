package com.d129cm.backendapi.item.manager;

import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.repository.ItemOptionRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class ItemOptionManagerTest {

    @InjectMocks
    private ItemOptionManager itemOptionManager;

    @Mock
    private ItemOptionRepository itemOptionRepository;

    @Nested
    class getItemOption{

        @Test
        void 성공반환_ItemOptionId로_아이템옵션_조회() {
            // given
            Long itemOptionId = 1L;
            ItemOption itemOption = ItemOption.builder()
                    .name("옵션 이름")
                    .quantity(100)
                    .optionPrice(1000)
                    .build();
            when(itemOptionRepository.findById(itemOptionId)).thenReturn(Optional.ofNullable(itemOption));

            // when
            ItemOption result = itemOptionManager.getItemOption(itemOptionId);

            // then
            assertThat(result).isEqualTo(itemOption);
        }
    }
}
