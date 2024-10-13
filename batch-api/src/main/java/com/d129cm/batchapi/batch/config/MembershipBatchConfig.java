package com.d129cm.batchapi.batch.config;

import com.d129cm.batchapi.batch.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class MembershipBatchConfig {

    @Bean
    public Job gradeMembership(JobRepository jobRepository, Step getAmountSpent) {
        return new JobBuilder("gradeMembership", jobRepository)
                .start(getAmountSpent)
                .build();
    }

    @Bean
    public Step getAmountSpentStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                   @Qualifier("coreDataSource") DataSource dataSource) {
        return new StepBuilder("getAmountSpentStep", jobRepository)
                .<MemberDto, MemberDto>chunk(100, transactionManager)
                .reader(jdbcCursorItemReader(dataSource))
                .writer(jdbcCursorItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<MemberDto> jdbcCursorItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<MemberDto>()
                .fetchSize(100)
                .dataSource(dataSource)
                .rowMapper(new DataClassRowMapper<>(MemberDto.class))
                .sql("SELECT * FROM member")
                .name("jdbcCursorItemReader")
                .build();
    }

    private ItemWriter<MemberDto> jdbcCursorItemWriter() {


        return list -> {
            for (MemberDto m : list) {
                System.out.println(m.toString());
            }
        };
    }
}