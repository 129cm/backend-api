package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ItemOptionFixture {

    public static ItemOption createItemOption(Item item) {
        ItemOption itemOption = spy(ItemOption.builder()
                .name("상품 옵션 이름")
                .quantity(100)
                .optionPrice(200)
                .build());
        item.addItemOption(itemOption);
        when(itemOption.getId()).thenReturn(5L);
        when(itemOption.getOptionPrice()).thenReturn(200);
        return itemOption;
    }
}
