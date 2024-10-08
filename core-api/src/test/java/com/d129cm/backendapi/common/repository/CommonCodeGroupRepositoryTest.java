package com.d129cm.backendapi.common.repository;

import com.d129cm.backendapi.common.annotation.JpaSliceTest;
import com.d129cm.backendapi.common.domain.CommonCodeGroup;
import com.d129cm.backendapi.common.domain.code.GroupName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JpaSliceTest
public class CommonCodeGroupRepositoryTest {

    @Autowired
    private CommonCodeGroupRepository commonCodeGroupRepository;

    @Nested
    @Sql("/test-common-code.sql")
    class FindByGroupCode {

        @Test
        void 배송코드반환_id를_이용해_조회() {

            // given
            GroupName groupName = GroupName.배송;

            // when
            Optional<CommonCodeGroup> commonCodeGroup = commonCodeGroupRepository.findById(groupName.getGroupId());

            // then
            assertThat(commonCodeGroup.isPresent()).isTrue();
            assertThat(commonCodeGroup.get().getGroupName()).isEqualTo(groupName);
            assertThat(commonCodeGroup.get().getGroupId()).isEqualTo(groupName.getGroupId());
        }
    }

}
