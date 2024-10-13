package com.d129cm.backendapi.member.domain;

import lombok.Getter;

@Getter
public enum MembershipLevel {
    VIP(100000),
    골드(50000),
    실버( 30000),
    브론즈(0);

    private final int threshold;

    MembershipLevel( int threshold) {
        this.threshold = threshold;
    }

    public static MembershipLevel getMembershipLevelByAmount(int amountSpent) {
        for(MembershipLevel level: MembershipLevel.values()) {
            if (amountSpent >= level.getThreshold()) {
                return level;
            }
        }
        return 브론즈;
    }
}
