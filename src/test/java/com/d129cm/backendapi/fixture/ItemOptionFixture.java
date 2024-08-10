package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;

import static org.mockito.Mockito.spy;

public class ItemOptionFixture {

    public static ItemOption createItemOption(Item item) {
        ItemOption itemOption = spy(ItemOption.builder()
                .name("상품 옵션 이름")
                .quantity(100)
                .optionPrice(200)
                .build());
        item.addItemOption(itemOption);
        return itemOption;
    }
}
