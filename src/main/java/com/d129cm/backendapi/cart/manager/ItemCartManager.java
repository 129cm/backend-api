package com.d129cm.backendapi.cart.manager;

import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.cart.repository.ItemCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ItemCartManager {

    private final ItemCartRepository itemCartRepository;

    public void createItemCart(ItemCart itemCart) {
        itemCartRepository.save(itemCart);
    }
}
