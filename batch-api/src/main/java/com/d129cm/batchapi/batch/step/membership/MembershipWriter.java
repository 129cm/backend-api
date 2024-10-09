package com.d129cm.batchapi.batch.step.membership;

import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MembershipWriter implements ItemWriter<Member> {

    private final MemberRepository memberRepository;

    @Override
    // 이 청크가 process에서 100명이 모여야 wirter로 전달이 되는 건지?
    public void write(Chunk<? extends Member> chunk) {
        // saveAll 하면 하나하나 다 select 한 다음에 update를 치나?
        memberRepository.saveAll(chunk.getItems());
        log.info("멤버 등급 업데이트 완료");

    }
}