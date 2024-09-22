package com.d129cm.backendapi.payment.service;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.order.manager.OrderItemOptionManager;
import com.d129cm.backendapi.order.manager.OrderManager;
import com.d129cm.backendapi.payment.manager.PaymentManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final OrderManager orderManager;
    private final OrderItemOptionManager orderItemOptionManager;
    private final PaymentManager paymentManager;

    public Integer getTotalPrice(Long orderId) {
        return paymentManager.getTotalPrice(orderId);
    }

    public void prepareOrder(Order order, String paymentKey) {
        order.updatePayAuthKey(paymentKey);
        decreaseStockQuantity(order.getId());
    }

    private void decreaseStockQuantity(Long orderId) {
        List<OrderItemOption> orderItemOptions = orderItemOptionManager.getOrderItemOptionByOrderId(orderId);
        for (OrderItemOption orderItemOption : orderItemOptions) {
            ItemOption itemOption = orderItemOption.getItemOption();
            itemOption.decreaseQuantity(orderItemOption.getCount());
        }
    }

    public void completeOrder(Long orderId) {
        List<OrderItemOption> orderItemOptions = orderItemOptionManager.getOrderItemOptionByOrderId(orderId);
        for (OrderItemOption orderItemOption : orderItemOptions) {
            orderItemOption.changeOrderState(new CommonCodeId(CodeName.결제완료));
        }
    }

    public void undoOrder(Long orderId) {
        List<OrderItemOption> orderItemOptions = orderItemOptionManager.getOrderItemOptionByOrderId(orderId);
        for (OrderItemOption orderItemOption : orderItemOptions) {
            orderItemOption.changeOrderState(new CommonCodeId(CodeName.주문취소));
        }
    }

    public Order getOrderByOrderSerial(String tossOrderId) {
        return orderManager.getOrderByOrderSerial(tossOrderId);
    }
}
