package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;

import static org.mockito.Mockito.spy;

public class ItemCartFixture {

    public static ItemCart createItemCart(Item item, ItemOption itemOption) {
        Cart cart = CartFixture.createCart(MemberFixture.createMember("user@example.com"));
        ItemCart itemCart = spy(ItemCart.builder()
                .count(1)
                .item(item)
                .itemOption(itemOption)
                .cart(cart)
                .build());
        return itemCart;
    }
}
