package com.d129cm.backendapi.order.domain;

import com.d129cm.backendapi.common.domain.BaseEntity;
import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.GroupName;
import com.d129cm.backendapi.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

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

    @Setter
    private String orderSerial;

    @Setter
    private String payAuthKey;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private Order(CommonCodeId commonCodeId, Member member) {
        Assert.isTrue(GroupName.주문.getGroupId().equals(commonCodeId.getGroupId()), "올바른 그룹 코드가 아닙니다.");
        this.commonCodeId = commonCodeId;
        this.member = member;
    }
}
