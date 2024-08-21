package com.d129cm.backendapi.item.manager;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.fixture.ItemFixture;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@ExtendWith(MockitoExtension.class)
public class ItemManagerTest {

    @InjectMocks
    private ItemManager itemManager;

    @Mock
    private ItemRepository itemRepository;

    private static final Long brandId = 1L;
    private static final int page = 0;
    private static final int size = 50;

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

            Page<Item> emptyPage = new PageImpl<>(List.of());
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
            Sort result = itemManager.createItemSort(sortCondition, sortOrder);

            // then
            assertThat(result).isEqualTo(Sort.by(sortOrder, sortCondition.getCondition()));
        }

        @Test
        void Sort반환_정렬_조건_가격오름차순() {
            // given
            SortCondition sortCondition = SortCondition.PRICE;
            Sort.Direction sortOrder = Sort.Direction.ASC;

            // when
            Sort result = itemManager.createItemSort(sortCondition, sortOrder);

            // then
            assertThat(result).isEqualTo(Sort.by(sortOrder, sortCondition.getCondition()));
        }
    }

    @Nested
    class createItem {

        @Test
        void 성공_아이템_생성() {
            // given
            Item item = new Item("ItemName", 1000, "image.jpg", "description");

            // when
            itemManager.createItem(item);

            // then
            verify(itemRepository, times(1)).save(item);
        }
    }


    @Nested
    class getItem {

        @Test
        void Item반환_아이템_조회() {
            // given
            Long itemId = 1L;
            Item item = Item.builder()
                    .name("아이템")
                    .image("이미지")
                    .price(10000)
                    .description("설명")
                    .build();
            when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(item));

            // when
            Item result = itemManager.getItem(itemId);

            // then
            assertThat(result).isEqualTo(item);
        }

        @Test
        void 에러반환_아이템_존재하지_않는_경우() {
            // given
            Long itemId = 1L;
            when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

            // when
            // then
            assertThrows(NotFoundException.class, () -> itemManager.getItem(itemId));
        }
    }

    @Nested
    class getItemForPartners {

        @Test
        void 성공_파트너스_아이템_조회() {
            // given
            Long partnersId = 1L;
            Pageable pageable = mock(Pageable.class);

            // when
            itemManager.getAllItemByPartnersId(partnersId, pageable);

            // then
            verify(itemRepository).findAllByPartnersId(any(Long.class), any(Pageable.class));
        }
    }

    @Nested
    class getItemByIdAndPartnersId {

        @Test
        void 성공_파트너스_아이템_상세조회() {
            // given
            Item item = spy(Item.builder()
                    .name("Item 1")
                    .price(10000)
                    .description("Item desc1")
                    .image("Item1.png").build());

            // when
            when(itemRepository.findByIdAndPartnersId(1L, 1L)).thenReturn(Optional.of(item));
            Item result = itemManager.getItemByIdAndPartnersId(1L, 1L);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Item 1");
        }

        @Test
        void 에러반환_조회_하려는_아이템이_없는_경우() {
            // when
            when(itemRepository.findByIdAndPartnersId(anyLong(), anyLong())).thenReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> itemManager.getItemByIdAndPartnersId(1L, 1L))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("정보를 찾을 수 없습니다.");
        }

        @Nested
        class deleteItem{

            @Test
            void 성공_아이템_삭제(){
                // given
                Item item = ItemFixture.createItem(mock(Brand.class));

                // when
                itemManager.deleteItem(item);

                // then
                verify(itemRepository).delete(item);
            }
        }

    }
}
