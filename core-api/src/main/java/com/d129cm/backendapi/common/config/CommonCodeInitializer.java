package com.d129cm.backendapi.common.config;

import com.d129cm.backendapi.common.domain.CommonCode;
import com.d129cm.backendapi.common.domain.CommonCodeGroup;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.domain.code.GroupName;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommonCodeInitializer implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {
        for (GroupName groupName : GroupName.values()) {
            CommonCodeGroup group = new CommonCodeGroup(groupName);
            entityManager.merge(group);
        }

        for (CodeName codeName : CodeName.values()) {
            CommonCode code = new CommonCode(codeName);
            entityManager.merge(code);
        }
    }
}
