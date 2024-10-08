package com.d129cm.backendapi.batch.step.membership;

import com.d129cm.backendapi.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
@Slf4j
public class MembershipCsvFileWriter implements ItemWriter<Member> {

    // 멤버가 각자 하루에 얼마 썼는지 파일로 저장하도록 바꾸기
    @Override
    public void write(Chunk<? extends Member> chunk) throws Exception {
        String tempFilePath = "temp_membership_levels.csv";
        String finalFilePath = "membership_levels.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath))) {
            writer.write("MemberId, MembershipLevel\n");

            for (Member member : chunk.getItems()) {
//                writer.write(member.getId() + "," + member.getMembershipLevel());
            }

            log.info("임시 파일에 기록 완료: " + tempFilePath);

            Files.move(Paths.get(tempFilePath), Paths.get(finalFilePath));

            log.info("최종 파일로 덮어쓰기 완료:  " + finalFilePath);
        } catch (IOException e) {
            log.error("파일 생성 중 오류 발생: " + e.getMessage());
        }
    }
}
