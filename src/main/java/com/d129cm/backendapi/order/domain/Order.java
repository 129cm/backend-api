package com.d129cm.backendapi.order.domain;

import com.d129cm.backendapi.common.domain.BaseEntity;
import com.d129cm.backendapi.common.domain.CommonCodeId;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "codeId", column = @Column(name = "code_id")),
            @AttributeOverride(name = "groupId", column = @Column(name = "group_id"))
    })
    private CommonCodeId commonCodeId;

    private String OrderSerial;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
