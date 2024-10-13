package com.d129cm.backendapi.item.domain;

public enum SortCondition {
    NEW("modifiedAt"), PRICE("price");

    String condition = "";

    SortCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
