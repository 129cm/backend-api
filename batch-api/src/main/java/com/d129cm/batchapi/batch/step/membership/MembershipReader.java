package com.d129cm.batchapi.batch.step.membership;


import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@AllArgsConstructor
public class MembershipReader implements ItemReader<Member> {

    private final MemberRepository memberRepository;
    private List<Member> memberList;
    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public Member read() throws Exception {
        // 1. 멤버를 끊어서 가져와야 한다
        // 2. 탈퇴한 회원을 어떻게 할 건지? 1차로 구현한 다음 -> 탈퇴구현 soft delete -> 탈퇴회원 처리
        // 3. concurrent hashmap 같은...??? 동시성 생각해볼 여지가 있는지
        // 저 memberList가 스레드간 공유되는... 병렬처리해야 하는 데이터일 텐데, 이 부분 한번 찾아보기
        if (memberList == null) {
            memberList = memberRepository.findAll();
        }
        if (index.get() < memberList.size()) {
            // 멤버를 청크로 보내야 하는 거 아닌가?
            return memberList.get(index.getAndIncrement());
        } else {
            return null;
        }
    }
}
