package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.partners.dto.GetItemDetailsResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class PartnersItemServiceTest {
    @InjectMocks
    private PartnersItemService partnersItemService;

    @Mock
    private ItemManager itemManager;

    @Nested
    class getItemDetails {

        @Test
        void 성공_파트너스_아이템_상세조회() {
            // given
            Long partnersId = 1L;
            Long itemId = 1L;
            Item item = Item.builder()
                    .name("Test Item")
                    .price(10000)
                    .image("image.png")
                    .description("Item Desc")
                    .build();

            when(itemManager.getItemByIdAndPartnersId(itemId, partnersId)).thenReturn(item);

            // when
            GetItemDetailsResponse response = partnersItemService.getItemDetails(itemId, partnersId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.itemName()).isEqualTo("Test Item");

            verify(itemManager, times(1)).getItemByIdAndPartnersId(itemId, partnersId);
        }
    }
}
