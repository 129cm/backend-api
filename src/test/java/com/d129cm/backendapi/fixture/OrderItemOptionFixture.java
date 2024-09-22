package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.partners.domain.Partners;

import static org.mockito.Mockito.mock;

public class OrderItemOptionFixture {

    private OrderItemOptionFixture() {
    }

    public static OrderItemOption makeOrderItemOption() {
        Member member = MemberFixture.createMember("aaa@example.com");
        Brand brand = BrandFixture.createBrand(mock(Partners.class));
        Item item = ItemFixture.createItem(brand);
        ItemOption itemOption = ItemOptionFixture.createItemOption(item);
        Order order = OrderFixture.makeOrderWithOrderSerial(member, "20240922-1234567");
        OrderItemOption orderItemOption = OrderItemOption.builder()
                .itemOption(itemOption)
                .count(1)
                .commonCodeId(new CommonCodeId(CodeName.결제완료))
                .order(order)
                .salesPrice(1000)
                .build();
        return orderItemOption;
    }
}
