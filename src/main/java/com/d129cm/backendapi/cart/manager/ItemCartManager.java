package com.d129cm.backendapi.cart.manager;

import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.cart.repository.ItemCartRepository;
import com.d129cm.backendapi.member.dto.CartItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class ItemCartManager {

    private final ItemCartRepository itemCartRepository;

    public void createItemCart(ItemCart itemCart) {
        itemCartRepository.save(itemCart);
    }

    public List<ItemCart> getItemCart(Long cartId) {
        return itemCartRepository.findAllByCartId(cartId);
    }

    public ItemCart findItemCart(CartItemRequest request, Long cartId) {
        return itemCartRepository.findByItemIdAndItemOptionIdAndCartId(request.itemId(), request.itemOptionId(), cartId);
    }

    public void increaseCount(ItemCart itemCart, int count) {
        itemCart.increaseCount(count);
        itemCartRepository.save(itemCart);
    }
}
