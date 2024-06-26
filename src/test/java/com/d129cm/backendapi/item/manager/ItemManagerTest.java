package com.d129cm.backendapi.item.manager;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.SortCondition;
import com.d129cm.backendapi.item.repository.ItemRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.DESC;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class ItemManagerTest {

    @InjectMocks
    private ItemManager itemManager;

    @Mock
    private ItemRepository itemRepository;

    private static Long brandId = 1L;
    private static int page = 0;
    private static int size = 50;

    @Nested
    class getBrand {

        @Test
        void 모든아이템반환_브랜드_조회() {
            // given
            Sort sortObj = Sort.by(DESC, SortCondition.NEW.getCondition());
            Pageable pageable = PageRequest.of(page, size, sortObj);

            Item item1 = mock(Item.class);
            Item item2 = mock(Item.class);
            Item item3 = mock(Item.class);

            Page<Item> itemPage = new PageImpl<>(Arrays.asList(item1, item2, item3));
            when(itemRepository.findAllByBrandId(brandId, pageable)).thenReturn(itemPage);

            // when
            Page<Item> result = itemManager.getAllItemByBrandId(brandId, pageable);

            // then
            assertThat(result).isEqualTo(itemPage);
            assertThat(result.getTotalElements()).isEqualTo(3);
        }

        @Test
        void 빈페이지반환_아이템이_없는_경우() {
            // given
            Sort sortObj = Sort.by(DESC, SortCondition.NEW.getCondition());
            Pageable pageable = PageRequest.of(page, size, sortObj);

            Page<Item> emptyPage = new PageImpl<>(Arrays.asList());
            when(itemRepository.findAllByBrandId(brandId, pageable)).thenReturn(emptyPage);

            // when
            Page<Item> result = itemManager.getAllItemByBrandId(brandId, pageable);

            // then
            assertThat(result).isEqualTo(emptyPage);
            assertThat(result.getTotalElements()).isEqualTo(0);
            assertThat(result.getContent()).isEmpty();
        }
    }

    @Nested
    class getSortObjectTest {

        @Test
        void Sort반환_정렬_조건_최신순() {
            // given
            SortCondition sortCondition = SortCondition.NEW;
            Sort.Direction sortOrder = Sort.Direction.DESC;

            // when
            Sort result = itemManager.getSortObject(sortCondition, sortOrder);

            // then
            assertThat(result).isEqualTo(Sort.by(sortOrder, sortCondition.getCondition()));
        }

        @Test
        void Sort반환_정렬_조건_가격오름차순() {
            // given
            SortCondition sortCondition = SortCondition.PRICE;
            Sort.Direction sortOrder = Sort.Direction.ASC;

            // when
            Sort result = itemManager.getSortObject(sortCondition, sortOrder);

            // then
            assertThat(result).isEqualTo(Sort.by(sortOrder, sortCondition.getCondition()));
        }
    }
}
