package com.d129cm.backendapi.cart.repository;

import com.d129cm.backendapi.cart.domain.ItemCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {
    List<ItemCart> findAllByCartId(Long cartId);

    ItemCart findByItemIdAndItemOptionIdAndCartId(Long itemId, Long itemOptionId, Long cartId);
}
