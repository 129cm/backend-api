package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.order.domain.OrderItemOptionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption, OrderItemOptionId> {
    List<OrderItemOption> findByOrderId(Long orderId);
}
