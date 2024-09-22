package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.*;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.order.domain.OrderItemOptionId;
import com.d129cm.backendapi.order.dto.CreateOrderDto;
import com.d129cm.backendapi.order.dto.OrderFormDto;
import com.d129cm.backendapi.order.manager.OrderItemOptionManager;
import com.d129cm.backendapi.order.manager.OrderManager;
import com.d129cm.backendapi.payment.manager.PaymentManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberOrderService {

    private final ItemManager itemManager;
    private final ItemOptionManager itemOptionManager;
    private final OrderManager orderManager;
    private final OrderItemOptionManager orderItemOptionManager;
    private final PaymentManager paymentManager;

    public OrderFormForMemberResponse getOrderForm(List<OrderFormDto> orderFormDto, Member member) {
        String username = member.getName();
        AddressResponse addressResponse = AddressResponse.of(member.getAddress());
        List<BrandsForOrderResponse> brandsForOrderResponses = makeBrandsForOrderResponses(orderFormDto);
        return new OrderFormForMemberResponse(username, addressResponse, brandsForOrderResponses);
    }

    private List<BrandsForOrderResponse> makeBrandsForOrderResponses(List<OrderFormDto> orderFormDto) {
        Map<Brand, List<ItemWithOptionForOrderResponse>> brandMap = makeBrandListMap(orderFormDto);

        List<BrandsForOrderResponse> brandsForOrderResponses = new ArrayList<>();
        for (Map.Entry<Brand, List<ItemWithOptionForOrderResponse>> entry : brandMap.entrySet()) {
            Brand brand = entry.getKey();
            List<ItemWithOptionForOrderResponse> itemList = entry.getValue();
            BrandsForOrderResponse brandResponse = BrandsForOrderResponse.of(brand, itemList);
            brandsForOrderResponses.add(brandResponse);
        }
        return brandsForOrderResponses;
    }

    private Map<Brand, List<ItemWithOptionForOrderResponse>> makeBrandListMap(List<OrderFormDto> orderFormDto) {
        Map<Brand, List<ItemWithOptionForOrderResponse>> brandMap = new HashMap<>();

        for (OrderFormDto dto : orderFormDto) {
            Item item = itemManager.getItem(dto.itemId());
            Brand brand = item.getBrand();
            ItemOption itemOption = itemOptionManager.getItemOption(dto.itemOptionId());
            Integer count = dto.count();
            validateCount(count);
            ItemWithOptionForOrderResponse itemResponse =
                    ItemWithOptionForOrderResponse.of(item, itemOption, count);
            brandMap.computeIfAbsent(brand, k -> new ArrayList<>()).add(itemResponse);
        }
        return brandMap;
    }

    private void validateCount(Integer count) {
        if (count < 0) throw BadRequestException.negativeQuantityLimit();
        if (count > 100) throw BadRequestException.exceedQuantityLimit(100);
    }

    public String createOrder(CreateOrderDto createOrderDto, Member member) {
        Order order = orderManager.createOrder(member);
        orderItemOptionManager.createOrderItemOption(order, createOrderDto);
        return order.getOrderSerial();
    }

    public List<MyOrderResponse> getMyOrders(Member member, int page, int size) {
        Sort sortObj = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<Order> orderList = orderManager.getOrdersByMemberId(member.getId(), pageable);

        return orderList.stream()
                .map(order -> {
                    List<OrderItemOption> orderItemOptionList
                            = orderItemOptionManager.getOrderItemOptionByOrderId(order.getId());
                    List<MyOrderDetailsResponse> itemInfoList = orderItemOptionList.stream()
                            .map(MyOrderDetailsResponse::of)
                            .collect(Collectors.toList());

                    return new MyOrderResponse(order.getId(), order.getOrderSerial(), order.getCreatedAt(), itemInfoList);

                })
                .collect((Collectors.toList()));
    }

    public MyOrderInfoResponse getMyOrderDetails(Member member, Long orderId) {
        Order order = orderManager.getOrderById(orderId);
        List<OrderItemOption> orderItemOptionList = orderItemOptionManager.getOrderItemOptionByOrderId(orderId);
        List<MyOrderDetailsResponse> itemInfoList = orderItemOptionList.stream()
                .map(MyOrderDetailsResponse::of)
                .collect(Collectors.toList());
        Integer totalPrice = paymentManager.getTotalPrice(orderId);
        OrderInfoResponse orderInfoResponse = new OrderInfoResponse(member.getName(), member.getEmail(), totalPrice);
        return MyOrderInfoResponse.of(order, itemInfoList, orderInfoResponse);
    }

    public void withdrawOrder(Long orderId, Long itemOptionId) {
        OrderItemOptionId orderItemOptionId = new OrderItemOptionId(orderId, itemOptionId);
        OrderItemOption orderItemOption = orderItemOptionManager.getOrderItemOptionId(orderItemOptionId);
        orderItemOption.changeOrderState(new CommonCodeId(CodeName.주문취소));
    }
}
