package com.d129cm.backendapi.order.domain;

import com.d129cm.backendapi.common.domain.BaseEntity;
import com.d129cm.backendapi.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderSerial;

    private String payAuthKey;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private Integer totalSalesPrice;

    public Order(Member member, String orderSerial) {
        this.member = member;
        this.orderSerial = orderSerial;
    }

    public void updatePayAuthKey(String payAuthKey) {
        this.payAuthKey = payAuthKey;
    }

    public void updateTotalSalesPrice(int totalSalesPrice) {
        this.totalSalesPrice = totalSalesPrice;
    }
}

