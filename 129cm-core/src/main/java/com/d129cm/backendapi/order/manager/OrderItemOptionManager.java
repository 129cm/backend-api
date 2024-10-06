package com.d129cm.backendapi.order.manager;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.dto.BrandsForOrderResponse;
import com.d129cm.backendapi.member.dto.ItemWithOptionForOrderResponse;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.order.domain.OrderItemOptionId;
import com.d129cm.backendapi.order.dto.CreateOrderDto;
import com.d129cm.backendapi.order.repository.OrderItemOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderItemOptionManager {
    private final OrderItemOptionRepository orderItemOptionRepository;
    private final ItemOptionManager itemOptionManager;

    public List<OrderItemOption> getOrderItemOptionByOrderId(Long orderId) {
        return orderItemOptionRepository.findByOrderId(orderId);
    }

    public void createOrderItemOption(Order order, CreateOrderDto createOrderDto) {
        int totalPrice = 0;

        for (BrandsForOrderResponse response : createOrderDto.brandsForOrderResponse()) {
            List<ItemWithOptionForOrderResponse> itemResponseList = response.itemResponse();
            for (ItemWithOptionForOrderResponse itemResponse : itemResponseList) {
                int salesPrice = itemResponse.itemPrice() + itemResponse.itemOptionPrice();

                OrderItemOption orderItemOption = OrderItemOption.builder()
                        .itemOption(itemOptionManager.getItemOption(itemResponse.itemOptionId()))
                        .order(order)
                        .salesPrice(salesPrice)
                        .count(itemResponse.count())
                        .commonCodeId(new CommonCodeId(CodeName.주문대기))
                        .build();

                totalPrice += salesPrice * itemResponse.count();
                orderItemOptionRepository.save(orderItemOption);
            }
        }

        order.updateTotalSalesPrice(totalPrice);
    }

    public OrderItemOption getOrderItemOptionId(OrderItemOptionId orderItemOptionId) {
        return orderItemOptionRepository.findById(orderItemOptionId).orElseThrow(NotFoundException::entityNotFound);
    }

    public int modifyOrderState(String codeId, List<OrderItemOptionId> ids) {
        return orderItemOptionRepository.updateOrderItemOptionsCommonCodeId(codeId, ids);
    }
}
