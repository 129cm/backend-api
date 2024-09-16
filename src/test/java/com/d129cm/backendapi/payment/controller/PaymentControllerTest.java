package com.d129cm.backendapi.payment.controller;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.config.TestSecurityConfig;
import com.d129cm.backendapi.fixture.MemberFixture;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.payment.service.PaymentService;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@Import(TestSecurityConfig.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private RestTemplate restTemplate;

    @Value("${payment.toss.test_secret_api_key}")
    String tossTestSecretApiKey;

    private Member member = MemberFixture.createMember("abc@example.com");
    private String paymentKey = "paymentKey";
    private String tossOrderId = "20240915-2345678";
    private Integer amount = 30000;

    @Nested
    class confirmPayment {

        @Test
        void 결제성공_toss_api_호출() throws Exception {
            // given
            Order mockOrder = mock(Order.class);
            when(paymentService.getOrderByOrderSerial(tossOrderId)).thenReturn(mockOrder);
            when(mockOrder.getId()).thenReturn(1L);
            when(paymentService.getTotalPrice(1L)).thenReturn(amount);

            doNothing().when(paymentService).prepareOrder(eq(mockOrder), eq(paymentKey));

            JsonObject successResponse = new JsonObject();
            successResponse.addProperty("status", "DONE");
            ResponseEntity<String> restTemplateResponse = ResponseEntity.ok(successResponse.toString());
            when(restTemplate.postForEntity(
                    eq("https://api.tosspayments.com/v1/payments/confirm"),
                    any(HttpEntity.class),
                    eq(String.class)
            )).thenReturn(restTemplateResponse);

            doNothing().when(paymentService).completeOrder(mockOrder);

            // when
            ResultActions result = mockMvc.perform(post("/members/payments/confirm")
                    .param("paymentKey", paymentKey)
                    .param("orderId", tossOrderId)
                    .param("amount", String.valueOf(amount))
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(spy(new MemberDetails(member))))
            );

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.isPaymentSuccess").value(true))
                    .andExpect(jsonPath("$.message").value("결제에 성공하였습니다!"));

            verify(paymentService).getOrderByOrderSerial(tossOrderId);
            verify(paymentService).getTotalPrice(1L);
            verify(paymentService).prepareOrder(mockOrder, paymentKey);
            verify(paymentService).completeOrder(mockOrder);
        }

        @Test
        void 결제실패_금액_불일치() throws Exception {
            // given
            Integer wrongAmount = 40000;

            Order mockOrder = mock(Order.class);
            when(paymentService.getOrderByOrderSerial(tossOrderId)).thenReturn(mockOrder);
            when(mockOrder.getId()).thenReturn(1L);
            when(paymentService.getTotalPrice(1L)).thenReturn(wrongAmount);

            // when
            ResultActions result = mockMvc.perform(post("/members/payments/confirm")
                    .param("paymentKey", paymentKey)
                    .param("orderId", tossOrderId)
                    .param("amount", String.valueOf(amount))
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(new MemberDetails(member)))
            );

            // then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("주문 정보가 상이합니다. 결제 금액(을)를 확인하세요."));
        }

        @Test
        void 결제실패_주문_조회_실패() throws Exception {
            // given
            when(paymentService.getOrderByOrderSerial(tossOrderId)).thenThrow(NotFoundException.entityNotFound());

            // when
            ResultActions result = mockMvc.perform(post("/members/payments/confirm")
                    .param("paymentKey", paymentKey)
                    .param("orderId", tossOrderId)
                    .param("amount", String.valueOf(amount))
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(new MemberDetails(member)))
            );

            // then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("정보를 찾을 수 없습니다."));
        }

        @Test
        void 결제실패_토스API호출실패() throws Exception {
            // given
            Order mockOrder = mock(Order.class);
            when(paymentService.getOrderByOrderSerial(tossOrderId)).thenReturn(mockOrder);
            when(mockOrder.getId()).thenReturn(1L);
            when(paymentService.getTotalPrice(1L)).thenReturn(amount);

            doNothing().when(paymentService).prepareOrder(eq(mockOrder), eq(paymentKey));

            JsonObject failureResponse = new JsonObject();
            failureResponse.addProperty("status", "FAILED");
            ResponseEntity<String> restTemplateResponse = ResponseEntity.badRequest().body(failureResponse.toString());
            when(restTemplate.postForEntity(
                    eq("https://api.tosspayments.com/v1/payments/confirm"),
                    any(HttpEntity.class),
                    eq(String.class)
            )).thenReturn(restTemplateResponse);

            // when
            ResultActions result = mockMvc.perform(post("/members/payments/confirm")
                    .param("paymentKey", paymentKey)
                    .param("orderId", tossOrderId)
                    .param("amount", String.valueOf(amount))
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(new MemberDetails(member)))
            );

            // then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.isPaymentSuccess").value(false))
                    .andExpect(jsonPath("$.message").value(containsString("결제 승인 처리가 정상적으로 완료되지 않았습니다.")));
        }

        @Test
        void 결제실패_주문완료처리중오류() throws Exception {
            // given
            Order mockOrder = mock(Order.class);
            when(paymentService.getOrderByOrderSerial(tossOrderId)).thenReturn(mockOrder);
            when(mockOrder.getId()).thenReturn(1L);
            when(paymentService.getTotalPrice(1L)).thenReturn(amount);

            doNothing().when(paymentService).prepareOrder(eq(mockOrder), eq(paymentKey));

            JsonObject successResponse = new JsonObject();
            successResponse.addProperty("status", "DONE");
            ResponseEntity<String> restTemplateResponse = ResponseEntity.ok(successResponse.toString());
            when(restTemplate.postForEntity(
                    eq("https://api.tosspayments.com/v1/payments/confirm"),
                    any(HttpEntity.class),
                    eq(String.class)
            )).thenReturn(restTemplateResponse);

            doThrow(new RuntimeException("주문 완료 처리 중 오류 발생")).when(paymentService).completeOrder(mockOrder);

            when(restTemplate.postForEntity(
                    eq("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"),
                    any(HttpEntity.class),
                    eq(String.class)
            )).thenReturn(ResponseEntity.ok(""));

            // when
            ResultActions result = mockMvc.perform(post("/members/payments/confirm")
                    .param("paymentKey", paymentKey)
                    .param("orderId", tossOrderId)
                    .param("amount", String.valueOf(amount))
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(new MemberDetails(member)))
            );

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.isPaymentSuccess").value(false))
                    .andExpect(jsonPath("$.message").value("결제 완료 처리 중 에러 발생하여 취소 API 호출을 완료했습니다."));
        }
    }
}
