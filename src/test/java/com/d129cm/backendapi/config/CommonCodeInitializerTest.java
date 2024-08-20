package com.d129cm.backendapi.config;

import com.d129cm.backendapi.common.config.CommonCodeInitializer;
import com.d129cm.backendapi.common.domain.CommonCode;
import com.d129cm.backendapi.common.domain.CommonCodeGroup;
import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.domain.code.GroupName;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportTestcontainers(InitializeTestContainers.class)
@Import(CommonCodeInitializer.class)
public class CommonCodeInitializerTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testCommonCodeInitializer() {
        CommonCodeGroup group = entityManager.find(CommonCodeGroup.class, "010"); // 예시로 주문의 ID인 "010"
        assertThat(group).isNotNull();
        assertThat(group.getGroupName()).isEqualTo(GroupName.주문);

        CommonCode code = entityManager.find(CommonCode.class, new CommonCodeId(CodeName.결제완료));
        assertThat(code).isNotNull();
        assertThat(code.getCodeName()).isEqualTo(CodeName.결제완료);
    }
}

