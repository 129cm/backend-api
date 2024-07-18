package com.d129cm.backendapi.cart.repository;

import com.d129cm.backendapi.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
