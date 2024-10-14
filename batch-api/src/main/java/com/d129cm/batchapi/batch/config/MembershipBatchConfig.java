package com.d129cm.batchapi.batch.config;

import com.d129cm.batchapi.batch.MemberTotalSpentDto;
import com.d129cm.batchapi.batch.exception.CustomSkipListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MembershipBatchConfig {
    @Bean
    public Job gradeMembership(JobRepository jobRepository, Step gradeMembershipStep) {
        return new JobBuilder("gradeMembership", jobRepository)
                .start(gradeMembershipStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step gradeMembershipStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                     @Qualifier("coreDataSource") DataSource dataSource) {
        return new StepBuilder("gradeMembershipStep", jobRepository)
                .<MemberTotalSpentDto, MemberTotalSpentDto>chunk(100, transactionManager)
                .reader(getAmountSpent(dataSource))
                .writer(saveTotalSpent())
                .faultTolerant() // 오류가 발생하더라도 실행을 중단하지 않고 오류 처리
                .skip(Exception.class) // 하위 예외 발생할 경우 해당 항목을 스킵하고 계속 실행
                .skipLimit(10) // 하지만 스킵 개수는 최대 10개까지. 그 이상이면 job 실패
                .listener(new CustomSkipListener("gradeMembershipStep")) // 예외 발생시 리스너가 지정된 로직 수행하도록
                .build();
    }

    @Bean
    public JdbcCursorItemReader<MemberTotalSpentDto> getAmountSpent(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<MemberTotalSpentDto>()
                .fetchSize(100)
                .dataSource(dataSource)
                .rowMapper(new DataClassRowMapper<>() {
                    @Override
                    public MemberTotalSpentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Long memberId = rs.getLong("member_id");
                        int totalSpent = rs.getInt("total_spent");
                        return new MemberTotalSpentDto(memberId, totalSpent);
                    }
                })
                .sql("select o.member_id, SUM(o.total_sales_price) AS total_spent " +
                        "from orders o " +
                        "where date(o.created_at) = CURRENT_DATE " +
                        "group by o.member_id")
                .name("getAmountSpentReader")
                .build();
    }

    private ItemWriter<MemberTotalSpentDto> saveTotalSpent() {
        return items -> {
            try(FileWriter writer = new FileWriter("total_spent.csv", true)) {
                for(MemberTotalSpentDto item : items) {
                    writer.append(String.format("%d,%d%n", item.id(), item.totalSpent()));
                }
            } catch (IOException e) {
                throw new RuntimeException("csv 파일 쓰기 도중 문제가 생겼습니다.");
            }
        };
    }
}