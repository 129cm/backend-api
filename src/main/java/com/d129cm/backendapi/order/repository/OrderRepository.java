package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderQueryRepository{
}
