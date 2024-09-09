package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.order.domain.OrderItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption, Long> {
    List<OrderItemOption> findByOrderId(Long orderId);
}
