package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.item.domain.Item;

import static org.mockito.Mockito.spy;

public class ItemFixture {
    public static Item createItem(Brand brand) {
        Item item = spy(Item.builder()
                .name("상품 이름")
                .price(1000)
                .image("상품 이미지")
                .description("상품 설명")
                .build());
        brand.addItem(item);
        return item;
    }
}
