package com.d129cm.backendapi.common.domain.code;

import lombok.Getter;

@Getter
public enum GroupName {

    주문("100"), 배송("200");

    private final String groupId;

    GroupName(String groupId) {
        this.groupId = groupId;
    }
}