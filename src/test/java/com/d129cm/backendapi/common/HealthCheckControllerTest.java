package com.d129cm.backendapi.common;

import com.d129cm.backendapi.auth.config.CommonSecurityConfig;
import com.d129cm.backendapi.common.controller.HealthCheckController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthCheckController.class)
@Import(CommonSecurityConfig.class)
@SuppressWarnings("NonAsciiCharacters")
public class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 성공200반환_헬스체크_성공() throws Exception {

        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.data").value("pong"));
    }
}
