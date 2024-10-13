package com.d129cm.batchapi.batch.step.membership;

import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.domain.MembershipLevel;
import com.d129cm.backendapi.member.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MembershipProcessor implements ItemProcessor<Member, Member> {
    private final MembershipService membershipService;

    @Override
    public Member process(Member member) {
        Integer amountSpent = membershipService.getAmountOfOrder(member.getId());
        MembershipLevel membershipLevel = MembershipLevel.getMembershipLevelByAmount(amountSpent);
        String membershipLevelCode = CodeName.valueOf(membershipLevel.name()).getCodeId();
        member.updateMembershipLevel(membershipLevelCode);
        return member;
    }
}