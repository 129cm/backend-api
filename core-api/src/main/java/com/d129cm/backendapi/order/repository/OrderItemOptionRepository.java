package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.order.domain.OrderItemOptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption, OrderItemOptionId> {
    List<OrderItemOption> findByOrderId(Long orderId);

    @Modifying
    @Query("UPDATE OrderItemOption oio SET oio.commonCodeId.codeId = :codeId WHERE oio.id IN :orderItemOptionIds")
    int updateOrderItemOptionsCommonCodeId(String codeId, List<OrderItemOptionId> orderItemOptionIds);
}
