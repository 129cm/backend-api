package com.d129cm.backendapi.cart.manager;

import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.cart.repository.ItemCartRepository;
import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.member.dto.CartItemRequest;
import com.d129cm.backendapi.member.dto.CartItemUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Optional<ItemCart> itemCartOptional= itemCartRepository.findByItemIdAndItemOptionIdAndCartId(request.itemId(), request.itemOptionId(), cartId);
        if (itemCartOptional.isEmpty()) {
            throw NotFoundException.entityNotFound();
        }
        return itemCartOptional.get();
    }

    public void increaseCount(ItemCart itemCart, int count) {
        itemCart.increaseCount(count);
        itemCartRepository.save(itemCart);
    }

    public void updateItemQuantityInCart(Cart cart, CartItemUpdateRequest request) {
        Integer count = request.count();
        Optional<ItemCart> itemCartOptional = itemCartRepository.findByItemIdAndItemOptionIdAndCartId(request.itemId(), request.itemOptionId(), cart.getId());
        if (itemCartOptional.isEmpty()) {
            throw NotFoundException.entityNotFound();
        }
        itemCartOptional.get().updateCount(count);
    }

    public void deleteItemFromCart(Cart cart, Long itemId, Long itemOptionId) {
        Optional<ItemCart> itemCartOptional = itemCartRepository.findByItemIdAndItemOptionIdAndCartId(itemId, itemOptionId, cart.getId());
        if (itemCartOptional.isEmpty()) {
            throw NotFoundException.entityNotFound();
        }
        itemCartRepository.delete(itemCartOptional.get());
    }
}
