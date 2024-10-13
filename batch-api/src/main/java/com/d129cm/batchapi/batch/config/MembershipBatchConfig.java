package com.d129cm.batchapi.batch.config;

import com.d129cm.batchapi.batch.step.membership.MembershipCsvFileWriter;
import com.d129cm.batchapi.batch.step.membership.MembershipWriter;
import com.d129cm.backendapi.member.domain.Member;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Configuration
public class MembershipBatchConfig {

    @Bean
    public Job gradeMembership(JobRepository jobRepository, Step getAmountSpent) {
        return new JobBuilder("gradeMembership", jobRepository)
                .start(getAmountSpent)
                .build();
    }

    // 스템 이름이 좀... 이상하네

    // 스텝 1 : 멤버의 주문 내역을 가져와서 읽고 하루의 결제금액을 파일로 저장한다.
    // 스텝 2 : 파일을 읽어서 등급을 매기고 이후 db에 저장한다.
    // tasklet이 실패하면, job을 재구동 -> step 재구동 -> 실패한 지점부터 tasklet을 수행하는지
    @Bean
    public Step getAmountSpentStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                   ItemReader<Member> reader, ItemProcessor<Member, Member> processor,
                                   MembershipWriter writer, MembershipCsvFileWriter csvWriter) {
        return new StepBuilder("getAmountSpentStep", jobRepository)
                .<Member, Member>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(new CompositeItemWriterBuilder<Member>()
                        .delegates(Arrays.asList(writer, csvWriter))
                        .build())
                .build();
    }
}