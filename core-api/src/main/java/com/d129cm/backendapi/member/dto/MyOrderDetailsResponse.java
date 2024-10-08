package com.d129cm.backendapi.member.dto;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.domain.code.GroupName;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.order.domain.OrderItemOption;

public record MyOrderDetailsResponse (
        Long brandId,
        String brandName,
        Long itemId,
        String itemName,
        Integer itemPrice,
        String itemImage,
        Long itemOptionId,
        String itemOptionName,
        Integer itemOptionPrice,
        Integer count,
        String orderState
){

    public static MyOrderDetailsResponse of(OrderItemOption orderItemOption) {
        ItemOption itemOption = orderItemOption.getItemOption();
        Item item = itemOption.getItem();
        Brand brand = item.getBrand();
        Integer count = orderItemOption.getCount();
        CommonCodeId commonCodeId = orderItemOption.getCommonCodeId();
        String orderState = CodeName.from(commonCodeId.getCodeId(), GroupName.주문);

        return new MyOrderDetailsResponse(
                brand.getId(),
                brand.getName(),
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getImage(),
                itemOption.getId(),
                itemOption.getName(),
                itemOption.getOptionPrice(),
                count,
                orderState
        );
    }
}
