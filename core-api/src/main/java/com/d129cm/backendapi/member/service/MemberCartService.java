package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.cart.manager.ItemCartManager;
import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.CartForMemberResponse;
import com.d129cm.backendapi.member.dto.CartItemRequest;
import com.d129cm.backendapi.member.dto.CartItemUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCartService {

    private final ItemManager itemManager;
    private final ItemOptionManager itemOptionManager;
    private final ItemCartManager itemCartManager;

    private static final int MAX_QUANTITY_FOR_CART = 100;

    public void addItemToCart(Member member, CartItemRequest request) {
        validateCount(request.count());

        Item item = itemManager.getItem(request.itemId());
        ItemOption itemOption = itemOptionManager.getItemOption(request.itemOptionId());
        Cart cart = member.getCart();

        Optional<ItemCart> itemCart = itemCartManager.findItemCart(request, cart.getId());

        if (itemCart.isPresent()) {
            itemCartManager.increaseCount(itemCart.get(), request.count());
        } else {
            ItemCart newItemCart = ItemCart.builder()
                    .count(request.count())
                    .item(item)
                    .itemOption(itemOption)
                    .cart(cart)
                    .build();
            itemCartManager.createItemCart(newItemCart);
        }
    }

    public List<CartForMemberResponse> getCart(Member member) {
        Cart cart = member.getCart();
        List<ItemCart> itemCarts = itemCartManager.getItemCart(cart.getId());
        List<CartForMemberResponse> responses = new ArrayList<>();
        for(ItemCart itemCart : itemCarts) {
            responses.add(CartForMemberResponse.of(itemCart));
        }
        return responses;
    }

    public void updateItemQuantityInCart (Member member, CartItemUpdateRequest request) {
        validateCount(request.count());
        Cart cart = member.getCart();
        itemCartManager.updateItemQuantityInCart(cart, request);
    }

    public void deleteItemFromCart(Member member, Long itemId, Long itemOptionId) {
        Cart cart = member.getCart();
        itemCartManager.deleteItemFromCart(cart, itemId, itemOptionId);
    }

    private static void validateCount(Integer request) {
        if (request <= 0) {
            throw BadRequestException.negativeQuantityLimit();
        } else if (request > MAX_QUANTITY_FOR_CART) {
            throw BadRequestException.exceedQuantityLimit(MAX_QUANTITY_FOR_CART);
        }
    }
}
