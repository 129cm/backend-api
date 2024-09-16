package com.d129cm.backendapi.partners.controller;

import com.d129cm.backendapi.auth.domain.PartnersDetails;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.order.dto.OrdersSearchResponseDto;
import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import com.d129cm.backendapi.order.service.OrderService;
import com.d129cm.backendapi.partners.domain.Partners;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartnersOrderController.class)
public class PartnersOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class SearchOrders {

        @Test
        void 성공200_주문_조회() throws Exception {
            // given
            OrdersSearchResponseDto order1 = new OrdersSearchResponseDto(1L, "User 1", LocalDateTime.now(), 1L, List.of(), "Completed");
            OrdersSearchResponseDto order2 = new OrdersSearchResponseDto(2L, "User 2", LocalDateTime.now(), 2L, List.of(), "Pending");
            OrdersSearchResultDto searchResult = new OrdersSearchResultDto(List.of(order1, order2), 2L);

            Password password = mock(Password.class);
            Partners mockPartners = spy(Partners.builder()
                    .email("testPartners@email.com")
                    .password(password)
                    .businessNumber("123-45-67890")
                    .build());

            when(orderService.searchResult(any(), any(), any(), any(), anyInt(), anyInt()))
                    .thenReturn(searchResult);

            // when
            ResultActions result = mockMvc.perform(get("/partners/brands/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(new PartnersDetails(mockPartners)))
                    .param("itemName", "")
                    .param("startDate", "")
                    .param("endDate", "")
                    .param("orderState", "")
                    .param("size", "10")
                    .param("page", "0"));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.orders[0].orderId").value(1L))
                    .andExpect(jsonPath("$.data.orders[1].orderId").value(2L))
                    .andExpect(jsonPath("$.data.total").value(2));
        }
    }
}
