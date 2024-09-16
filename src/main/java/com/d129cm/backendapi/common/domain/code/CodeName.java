package com.d129cm.backendapi.common.domain.code;

import lombok.Getter;

@Getter
public enum CodeName {

    // 주문 (100)
    주문대기("000", GroupName.주문), 결제완료("010", GroupName.주문), 주문완료("020", GroupName.주문), 주문취소("030", GroupName.주문),

    // 배송 (200)
    배송중("010", GroupName.배송), 배송완료("020", GroupName.배송),

    // 등급 (300)
    실버("010", GroupName.회원등급), 골드("020", GroupName.회원등급), VIP("030", GroupName.회원등급);

    private final String codeId;
    private final GroupName groupName;

    CodeName(String codeId, GroupName groupName) {
        this.codeId = codeId;
        this.groupName = groupName;
    }
}