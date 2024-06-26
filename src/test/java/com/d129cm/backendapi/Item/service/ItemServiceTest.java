package com.d129cm.backendapi.Item.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.manager.BrandManager;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.dto.ItemCreateRequest;
import com.d129cm.backendapi.item.service.ItemService;
import com.d129cm.backendapi.partners.domain.Partners;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private BrandManager brandManager;

    @Nested
    class createItem{

        @Test
        void 성공_아이템_생성(){
            // given
            Partners mockPartners = mock(Partners.class);
            ItemCreateRequest mockRequest = mock(ItemCreateRequest.class);
            Item mockItem = mock(Item.class);
            Brand mockBrand = mock(Brand.class);
            when(mockRequest.toItemEntity()).thenReturn(mockItem);
            when(mockPartners.getBrand()).thenReturn(mockBrand);
            doNothing().when(brandManager).updateBrandItem(mockBrand, mockItem);

            // when
            itemService.createItem(mockPartners, mockRequest);

            // then
            verify(brandManager).updateBrandItem(mockBrand, mockItem);
        }
    }
}
