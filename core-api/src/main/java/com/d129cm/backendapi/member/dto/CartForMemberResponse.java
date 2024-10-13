package com.d129cm.backendapi.member.dto;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import lombok.Builder;

@Builder
public record CartForMemberResponse(
        Long brandId,
        String brandName,
        Long itemId,
        String itemName,
        Integer price,
        Integer count,
        Long itemOptionId,
        String itemOptionName,
        Integer itemOptionPrice
) {

    public static CartForMemberResponse of(ItemCart itemCart) {
        Item item = itemCart.getItem();
        ItemOption itemOption = itemCart.getItemOption();
        Brand brand = item.getBrand();

        return CartForMemberResponse.builder()
                .brandId(brand.getId())
                .brandName(brand.getName())
                .itemId(item.getId())
                .itemName(item.getName())
                .price(item.getPrice())
                .count(itemCart.getCount())
                .itemOptionId(itemOption.getId())
                .itemOptionName(itemOption.getName())
                .itemOptionPrice(itemOption.getOptionPrice())
                .build();
    }
}
