package com.d129cm.backendapi.common.domain.code;

import lombok.Getter;

@Getter
public enum CodeName {

    // 주문 (010)
    결제완료("010", GroupName.주문), 주문완료("020", GroupName.주문), 주문취소("030", GroupName.주문),

    // 배송 (020)
    배송중("010", GroupName.배송), 배송완료("020", GroupName.배송);

    private final String codeId;
    private final GroupName groupName;

    CodeName(String codeId, GroupName groupName) {
        this.codeId = codeId;
        this.groupName = groupName;
    }
}