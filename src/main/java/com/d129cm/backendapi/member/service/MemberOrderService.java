package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.AddressResponse;
import com.d129cm.backendapi.member.dto.BrandsForOrderResponse;
import com.d129cm.backendapi.member.dto.ItemWithOptionForOrderResponse;
import com.d129cm.backendapi.member.dto.OrderFormForMemberResponse;
import com.d129cm.backendapi.order.dto.OrderFormDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberOrderService {

    private final ItemManager itemManager;
    private final ItemOptionManager itemOptionManager;

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
}
