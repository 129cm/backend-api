package com.d129cm.backendapi.common.repository;

import com.d129cm.backendapi.common.annotation.JpaSliceTest;
import com.d129cm.backendapi.common.domain.CommonCode;
import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.domain.code.GroupName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JpaSliceTest
@Sql({"/clean-up.sql", "/test-common-code.sql"})
public class CommonCodeRepositoryTest {

    @Autowired
    private CommonCodeRepository commonCodeRepository;

    @Nested
    class findByCommonCodeId {

        @Test
        void 주문대기코드조회_CommonCodeId를_이용해_조회() {
            // given
            CommonCodeId id = new CommonCodeId(CodeName.결제완료);

            // when
            Optional<CommonCode> code = commonCodeRepository.findById(id);

            // then
            assertThat(code.isPresent()).isTrue();
            assertThat(code.get().getId().getCodeId()).isEqualTo(id.getCodeId());
            assertThat(code.get().getId().getGroupId()).isEqualTo(id.getGroupId());
            assertThat(code.get().getCodeName()).isEqualTo(CodeName.결제완료);
            assertThat(code.get().getGroupCode().getGroupName()).isEqualTo(GroupName.주문);
        }
    }
}
