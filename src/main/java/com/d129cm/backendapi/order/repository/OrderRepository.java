package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderSerial(String orderSerial);

    boolean existsByOrderSerial(String orderSerial);
}
