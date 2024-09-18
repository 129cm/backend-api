package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;

public class OrderFixture {

    private OrderFixture() {
    }

    public static Order makeOrderWithOrderSerial(Member member, String orderSerial) {
        Order order = new Order(member, orderSerial);
        return order;
    }
}
