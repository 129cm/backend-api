package com.d129cm.backendapi.common.aop;

import com.d129cm.backendapi.common.controller.HealthCheckController;
import com.d129cm.backendapi.common.exception.*;
import com.d129cm.backendapi.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthCheckController.class)
@Import({GlobalExceptionHandler.class, TestSecurityConfig.class})
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthCheckController controller;

    @Test
    void 실패409반환_ConflictException_발생() throws Exception {
        // given
        String fieldName = "fieldName";
        String fieldValue = "fieldValue";
        String message = String.format("중복된 %s: '%s'은 중복된 값 입니다.", fieldName, fieldValue);

        // when
        when(controller.healthCheck()).thenThrow(new ConflictException(message));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value("중복된 fieldName: 'fieldValue'은 중복된 값 입니다."));
    }

    @Test
    void 실패401반환_AuthenticationException_발생() throws Exception {
        // given
        String token = "Token";
        String message = String.format("토큰 : '%s'는 유효하지 않습니다.", token);

        // when
        when(controller.healthCheck()).thenThrow(new AuthenticationException(message));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.message").value("토큰 : 'Token'는 유효하지 않습니다."));
    }

    @Test
    void 실패400반환_BadRequestException_발생() throws Exception {
        // given
        int limitNumber = 100;
        String message = String.format("적용할 수 있는 수량 %s(을)를 초과하였습니다", limitNumber);

        // when
        when(controller.healthCheck()).thenThrow(new BadRequestException(message));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void 실패500반환_BaseException_발생() throws Exception {
        // given
        // when
        when(controller.healthCheck()).thenThrow(new BaseException("baseException", HttpStatus.INTERNAL_SERVER_ERROR));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value("baseException"));
    }

    @Test
    void 실패500반환_Exception_발생() throws Exception {
        // given
        // when
        when(controller.healthCheck()).thenThrow(new RuntimeException("exception"));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value("INTERNAL_SERVER_ERROR"));
    }
}
