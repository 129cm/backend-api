package com.d129cm.backendapi.member.domain;

import com.d129cm.backendapi.order.domain.Order;
import jakarta.persistence.*;

@Entity
public class MemberOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
