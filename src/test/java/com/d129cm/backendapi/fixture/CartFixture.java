package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.member.domain.Member;

import static org.mockito.Mockito.spy;

public class CartFixture {
    public static Cart createCart(Member member) {
        Cart cart = spy(new Cart());
        member.setCart(cart);
        return cart;
    }
}
