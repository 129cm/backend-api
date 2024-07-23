package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.manager.BrandManager;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.SortCondition;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.member.dto.BrandsForMemberResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberBrandServiceTest {

    @InjectMocks
    private MemberBrandService memberBrandService;

    @Mock
    private BrandManager brandManager;

    @Mock
    private ItemManager itemManager;

    @Nested
    class getBrands {
        @Test
        void BrandsForMemberResponse반환_멤버_브랜드_조회() {
            // given
            Long brandId = 1L;
            SortCondition sort = SortCondition.NEW;
            Sort.Direction sortOrder = Sort.Direction.DESC;

            Brand mockBrand = mock(Brand.class);
            when(mockBrand.getName()).thenReturn("브랜드 이름");
            when(mockBrand.getImage()).thenReturn("브랜드 이미지");
            when(mockBrand.getDescription()).thenReturn("브랜드 설명");

            List<Item> mockItems = List.of(mock(Item.class), mock(Item.class));
            Page<Item> mockItemPage = new PageImpl<>(mockItems);

            when(brandManager.getBrand(brandId)).thenReturn(mockBrand);
            when(itemManager.createItemSort(sort, sortOrder)).thenReturn(mock(Sort.class));
            when(itemManager.getAllItemByBrandId(eq(brandId), any(Pageable.class))).thenReturn(mockItemPage);

            // when
            BrandsForMemberResponse response = memberBrandService.getBrandsForMember(sort, sortOrder, brandId, 0, 50);

            // then
            verify(brandManager).getBrand(brandId);
            verify(itemManager).createItemSort(sort, sortOrder);
            verify(itemManager).getAllItemByBrandId(eq(brandId), any(Pageable.class));

            assertThat(response).isNotNull();
            assertThat(response.brandName()).isEqualTo("브랜드 이름");
            assertThat(response.brandImage()).isEqualTo("브랜드 이미지");
            assertThat(response.brandDescription()).isEqualTo("브랜드 설명");
            assertThat(response.itemResponse()).hasSize(2);
        }
    }
}
