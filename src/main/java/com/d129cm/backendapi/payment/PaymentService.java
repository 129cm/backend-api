package com.d129cm.backendapi.payment;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.order.manager.OrderItemOptionManager;
import com.d129cm.backendapi.order.manager.OrderManager;
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

    public Integer getTotalPrice (Long orderId) {
        List<OrderItemOption> orderItemOptions = orderItemOptionManager.getOrderItemOptionByOrderId(orderId);
        Integer totalPrice = 0;
        for (OrderItemOption orderItemOption : orderItemOptions) {
            totalPrice += orderItemOption.getSalesPrice();
        }
        return totalPrice;
    }

    public void prepareOrder(Order order, String tossOrderId, String paymentKey) {
        order.setPayAuthKey(paymentKey);
        decreaseStockQuantity(order.getId());
    }

    private void decreaseStockQuantity(Long orderId) {
        List<OrderItemOption> orderItemOptions = orderItemOptionManager.getOrderItemOptionByOrderId(orderId);
        for (OrderItemOption orderItemOption : orderItemOptions) {
            ItemOption itemOption = orderItemOption.getItemOption();
            Integer quantity = itemOption.getQuantity();
            quantity -= orderItemOption.getCount();
        }
    }

    public void completeOrder(Order order) {
        order.changeOrderState(new CommonCodeId(CodeName.결제완료));
    }

    public void undoOrder(Order order) {
        order.changeOrderState(new CommonCodeId(CodeName.주문취소));
    }

    public Order getOrderByOrderSerial(String tossOrderId) {
        return orderManager.getOrderByOrderSerial(tossOrderId);
    }
}
