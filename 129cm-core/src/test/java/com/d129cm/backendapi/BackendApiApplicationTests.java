package com.d129cm.backendapi;

import com.d129cm.backendapi.config.InitializeTestContainers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

@SpringBootTest
@ImportTestcontainers(InitializeTestContainers.class)
class BackendApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
