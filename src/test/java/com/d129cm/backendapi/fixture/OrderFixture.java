package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;

public class OrderFixture {

    private OrderFixture() {}

    public static Order makeOrder(Member member) {
        return Order.builder()
                .member(member)
                .commonCodeId(new CommonCodeId(CodeName.주문대기))
                .build();
    }

    public static Order makeOrderWithOrderSerial(Member member, String orderSerial) {
        Order order = Order.builder()
                .member(member)
                .commonCodeId(new CommonCodeId(CodeName.주문대기))
                .build();
        order.setOrderSerial(orderSerial);
        return order;
    }
}
