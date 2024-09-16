package com.d129cm.backendapi.order.manager;

import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.dto.BrandsForOrderResponse;
import com.d129cm.backendapi.member.dto.ItemWithOptionForOrderResponse;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
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
        for(BrandsForOrderResponse response: createOrderDto.brandsForOrderResponse()) {
            List<ItemWithOptionForOrderResponse> itemResponseList = response.itemResponse();
            for(ItemWithOptionForOrderResponse itemResponse: itemResponseList) {
                OrderItemOption orderItemOption = OrderItemOption.builder()
                        .itemOption(itemOptionManager.getItemOption(itemResponse.itemOptionId()))
                        .order(order)
                        .salesPrice(itemResponse.itemPrice() + itemResponse.itemOptionPrice())
                        .count(itemResponse.count())
                        .build();
                orderItemOptionRepository.save(orderItemOption);
            }
        }
    }
}
