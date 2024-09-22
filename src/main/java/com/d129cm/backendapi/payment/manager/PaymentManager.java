package com.d129cm.backendapi.payment.manager;

import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.order.manager.OrderItemOptionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentManager {

    private final OrderItemOptionManager orderItemOptionManager;

    public Integer getTotalPrice(Long orderId) {
        List<OrderItemOption> orderItemOptions = orderItemOptionManager.getOrderItemOptionByOrderId(orderId);
        Integer totalPrice = 0;
        for (OrderItemOption orderItemOption : orderItemOptions) {
            totalPrice += orderItemOption.getSalesPrice();
        }
        return totalPrice;
    }
}
