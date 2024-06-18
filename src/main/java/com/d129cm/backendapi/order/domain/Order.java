package com.d129cm.backendapi.order.domain;

import com.d129cm.backendapi.common.domain.BaseEntity;
import com.d129cm.backendapi.member.domain.MemberOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "orders")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus OrderStatus;
    @Column(nullable = false)
    private String zipCode;
    @Column(nullable = false)
    private String roadNameAddress;
    @Column
    private String addressDetails;
    @Column
    private Integer totalPrice;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<MemberOrder> memberOrders;
}
