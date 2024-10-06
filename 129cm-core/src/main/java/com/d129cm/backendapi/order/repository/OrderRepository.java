package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderQueryRepository {
    Optional<Order> findByOrderSerial(String orderSerial);

    boolean existsByOrderSerial(String orderSerial);

    @Query (value = "select o from Order o " +
            "join OrderItemOption oio " +
            "on oio.order.id = o.id " +
            "where o.member.id = :memberId")
    Page<Order> findOrderByMemberId(Long memberId, Pageable pageable);
}