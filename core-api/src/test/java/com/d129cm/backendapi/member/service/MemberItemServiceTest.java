package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.member.dto.ItemForMemberResponse;
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
public class MemberItemServiceTest {


    @InjectMocks
    private MemberItemService memberItemService;

    @Mock
    private ItemManager itemManager;

    @Nested
    class getItem {
        @Test
        void ItemForMemberRespons반환_멤버_아이템_조회() {
            // given
            Long itemId = 1L;
            Item item = mock(Item.class);
            Brand brand = mock(Brand.class);
            ItemOption itemOption1 = mock(ItemOption.class);
            ItemOption itemOption2 = mock(ItemOption.class);
            List<ItemOption> itemOptions = List.of(itemOption1, itemOption2);

            when(item.getId()).thenReturn(1L);
            when(item.getName()).thenReturn("아이템명");
            when(item.getPrice()).thenReturn(1000);
            when(item.getImage()).thenReturn("아이템 이미지");

            when(itemManager.getItem(itemId)).thenReturn(item);
            when(item.getBrand()).thenReturn(brand);
            when(item.getItemOptions()).thenReturn(itemOptions);

            // when
            ItemForMemberResponse response = memberItemService.getItemForMember(itemId);

            // then
            verify(itemManager).getItem(itemId);
            verify(item).getBrand();
            verify(item).getItemOptions();

            assertThat(response).isNotNull();
            assertThat(response.itemId()).isEqualTo(1L);
            assertThat(response.itemName()).isEqualTo("아이템명");
            assertThat(response.itemPrice()).isEqualTo(1000);
            assertThat(response.itemImage()).isEqualTo("아이템 이미지");
            assertThat(response.itemOptionResponse()).hasSize(2);
        }
    }
}
