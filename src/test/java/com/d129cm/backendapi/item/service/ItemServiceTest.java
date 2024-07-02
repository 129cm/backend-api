package com.d129cm.backendapi.item.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.manager.BrandManager;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.dto.ItemCreateRequest;
import com.d129cm.backendapi.item.dto.ItemOptionCreateRequest;
import com.d129cm.backendapi.partners.domain.Partners;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private BrandManager brandManager;

    @Nested
    class createItem {

        @Test
        void 성공_아이템_생성() {
            // given
            Partners mockPartners = mock(Partners.class);
            ItemOptionCreateRequest optionRequest = new ItemOptionCreateRequest("옵션명", 24, 1000);
            List<ItemOptionCreateRequest> optionCreateRequests = List.of(optionRequest);
            ItemCreateRequest request = new ItemCreateRequest("아이템", 10000, optionCreateRequests, "아이템사진", "아이템설명");

            Brand mockBrand = mock(Brand.class);

            when(mockPartners.getBrand()).thenReturn(mockBrand);

            // when
            itemService.createItem(mockPartners, request);

            // then
            verify(brandManager).updateBrandItem(eq(mockBrand), any(Item.class));
        }
    }
}
