package com.d129cm.backendapi.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j(topic = "Scheduler")
@Component
@RequiredArgsConstructor
// 스케쥴러 라이브러리 필요한지 찾아보기 + 뭘할 수 있을까? 쿼츠? qurtz 찾아보기
public class MembershipBatchScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Scheduled(cron = "0 0 1 * * *") // 매일 자정 1시
    public void assignMembership() throws Exception {
        log.info("멤버 등급 업데이트 실행");

        // 이렇게 하면 JobName이 JobInstance 테이블에 저장되나?
        Job job = jobRegistry.getJob("gradeMembership");
        JobParameters jobParameters = new JobParametersBuilder()
                // 여기에 보통 뭘 넣는지 확인해야 함
                .addLong("time", System.currentTimeMillis())
                .addString("date", LocalDate.now().toString())
                .toJobParameters();

        jobLauncher.run(job, jobParameters);
    }
}

