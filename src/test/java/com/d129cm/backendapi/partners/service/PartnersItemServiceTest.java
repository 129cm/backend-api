package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.partners.dto.GetItemDetailsResponse;
import com.d129cm.backendapi.partners.dto.PutItemDetailsRequest;
import com.d129cm.backendapi.partners.dto.PutItemOptionRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

    @Nested
    class putItemDetails {

        @Test
        void 성공_파트너스_아이템_정보_수정() {
            // given
            PutItemOptionRequest optionRequest1 = new PutItemOptionRequest(1L, "Small", 10, 0);
            PutItemOptionRequest optionRequest2 = new PutItemOptionRequest(2L, "Large", 10, 1000);
            List<PutItemOptionRequest> options = List.of(optionRequest1, optionRequest2);
            PutItemDetailsRequest request = new PutItemDetailsRequest("옷", 10000, "옷.png", "설명", options);
            Long partnersId = 1L;
            Long itemId = 1L;

            Item oldItem = new Item("기존 아이템", 5000, "기존.png", "기존 설명");
            Item newItem = request.toItemEntity();

            when(itemManager.getItemByIdAndPartnersId(itemId, partnersId)).thenReturn(oldItem);

            // when
            partnersItemService.putItemDetails(partnersId, itemId, request);

            // then
            verify(itemManager).getItemByIdAndPartnersId(itemId, partnersId);
            assertThat(newItem.getName()).isEqualTo(oldItem.getName());
            assertThat(newItem.getPrice()).isEqualTo(oldItem.getPrice());
            assertThat(newItem.getImage()).isEqualTo(oldItem.getImage());
            assertThat(newItem.getDescription()).isEqualTo(oldItem.getDescription());
        }
    }

    @Nested
    class deleteItem{

        @Test
        void 성공_아이템_삭제(){
            // given
            Item item = mock(Item.class);
            when(itemManager.getItemByIdAndPartnersId(1L, 1L)).thenReturn(item);

            // when
            partnersItemService.deleteItem(1L, 1L);

            // then
            verify(itemManager).deleteItem(item);
        }
    }
}
