package com.d129cm.backendapi.item.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.manager.BrandManager;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.domain.SortCondition;
import com.d129cm.backendapi.item.dto.ItemCreateRequest;
import com.d129cm.backendapi.item.dto.ItemOptionCreateRequest;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.GetItemsForPartnersResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private BrandManager brandManager;

    @Mock
    private ItemManager itemManager;

    @Nested
    class createItem {

        @Test
        void 성공_아이템_생성() {
            // given
            Partners partners = mock(Partners.class);
            Brand brand = mock(Brand.class);
            when(brandManager.getBrandWithItems(partners)).thenReturn(brand);

            ItemOptionCreateRequest optionRequest1 = new ItemOptionCreateRequest("옵션1", 10, 100);
            ItemOptionCreateRequest optionRequest2 = new ItemOptionCreateRequest("옵션2", 20, 200);
            ItemCreateRequest request = new ItemCreateRequest("아이템이름", 1000, List.of(optionRequest1, optionRequest2), "이미지.jpg", "설명");

            // when
            itemService.createItem(partners, request);

            // then
            verify(brandManager).getBrandWithItems(partners);
            ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
            verify(itemManager).createItem(itemCaptor.capture());
            Item capturedItem = itemCaptor.getValue();

            assertThat(capturedItem.getName()).isEqualTo("아이템이름");
            assertThat(capturedItem.getPrice()).isEqualTo(1000);
            assertThat(capturedItem.getImage()).isEqualTo("이미지.jpg");
            assertThat(capturedItem.getDescription()).isEqualTo("설명");
            assertThat(capturedItem.getItemOptions()).hasSize(2);

            ItemOption capturedOption1 = capturedItem.getItemOptions().get(0);
            assertThat("옵션1").isEqualTo(capturedOption1.getName());
            assertThat(10).isEqualTo(capturedOption1.getQuantity());
            assertThat(100).isEqualTo(capturedOption1.getOptionPrice());

            ItemOption capturedOption2 = capturedItem.getItemOptions().get(1);
            assertThat("옵션2").isEqualTo(capturedOption2.getName());
            assertThat(20).isEqualTo(capturedOption2.getQuantity());
            assertThat(200).isEqualTo(capturedOption2.getOptionPrice());
        }
    }

    @Nested
    class getItemsForPartners {

        @Test
        void 성공_파트너스_아이템_조회() {
            // given
            Partners partners = mock(Partners.class);
            when(partners.getId()).thenReturn(1L);

            SortCondition sortCondition = SortCondition.NEW;
            Sort.Direction sortOrder = Sort.Direction.ASC;
            int page = 0;

            Sort sort = Sort.by(sortOrder, sortCondition.getCondition());
            when(itemManager.createItemSort(any(SortCondition.class), any(Sort.Direction.class))).thenReturn(sort);

            Brand brand = Brand.builder()
                    .name("Brand A")
                    .partners(partners)
                    .image("imageA.jpg")
                    .description("Description A")
                    .build();

            Item item1 = Item.builder()
                    .name("Item 1")
                    .price(1000)
                    .image("image1.jpg")
                    .description("Description 1")
                    .build();
            item1.updateBrand(brand);

            Item item2 = Item.builder()
                    .name("Item 2")
                    .price(2000)
                    .image("image2.jpg")
                    .description("Description 2")
                    .build();
            item2.updateBrand(brand);

            Page<Item> mockPage = new PageImpl<>(List.of(item1, item2), PageRequest.of(page, 50), 2);
            when(itemManager.getAllItemByPartnersId(any(Long.class), any(Pageable.class))).thenReturn(mockPage);

            // when
            GetItemsForPartnersResponse response = itemService.getItemsForPartners(partners, sortCondition, sortOrder, page);

            // then
            verify(itemManager).getAllItemByPartnersId(any(Long.class), any(Pageable.class));

            assertThat(response.itemResponses()).hasSize(2);

            assertThat(response.itemResponses().get(0).itemName()).isEqualTo("Item 1");
            assertThat(response.itemResponses().get(0).price()).isEqualTo(1000);
            assertThat(response.itemResponses().get(0).itemImage()).isEqualTo("image1.jpg");
            assertThat(response.itemResponses().get(0).brandName()).isEqualTo("Brand A");

            assertThat(response.itemResponses().get(1).itemName()).isEqualTo("Item 2");
            assertThat(response.itemResponses().get(1).price()).isEqualTo(2000);
            assertThat(response.itemResponses().get(1).itemImage()).isEqualTo("image2.jpg");
            assertThat(response.itemResponses().get(1).brandName()).isEqualTo("Brand A");
        }
    }
}