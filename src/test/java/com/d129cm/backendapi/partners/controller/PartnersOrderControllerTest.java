package com.d129cm.backendapi.partners.controller;

import com.d129cm.backendapi.auth.domain.PartnersDetails;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.order.dto.OrderAddressDto;
import com.d129cm.backendapi.order.dto.OrderDetailsDto;
import com.d129cm.backendapi.order.dto.OrdersSearchResponseDto;
import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.service.PartnersOrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    private PartnersOrderService partnersOrderService;

    @Nested
    class SearchOrders {

        @Test
        void 성공200_주문_조회() throws Exception {
            // given
            OrdersSearchResponseDto order1 = new OrdersSearchResponseDto(1L, "User 1", LocalDateTime.now(), 1L, List.of());
            OrdersSearchResponseDto order2 = new OrdersSearchResponseDto(2L, "User 2", LocalDateTime.now(), 2L, List.of());
            OrdersSearchResultDto searchResult = new OrdersSearchResultDto(List.of(order1, order2), 2L);

            Password password = mock(Password.class);
            Partners mockPartners = spy(Partners.builder()
                    .email("testPartners@email.com")
                    .password(password)
                    .businessNumber("123-45-67890")
                    .build());

            when(partnersOrderService.searchResult(any(), any(), any(), any(), anyInt(), anyInt()))
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

    @Nested
    class GetOrderDetailsByOrderId {
        @Test
        @DisplayName("주문 상세 정보 조회 API 테스트")
        public void getOrderDetailsByOrderId_success() throws Exception {
            // given
            Password password = mock(Password.class);
            Partners mockPartners = spy(Partners.builder()
                    .email("testPartners@email.com")
                    .password(password)
                    .businessNumber("123-45-67890")
                    .build());

            OrderDetailsDto orderDetailsDto = new OrderDetailsDto(
                    1L, LocalDateTime.now(),
                    List.of(),
                    1L, "John Doe",
                    new OrderAddressDto("아파트 101호", "서울시 강남구", "12345")
            );

            Mockito.when(partnersOrderService.getOrderDetailsByOrderId(anyLong()))
                    .thenReturn(orderDetailsDto);

            // when & then
            mockMvc.perform(get("/partners/brands/orders/{orderId}", 1L)
                            .with(SecurityMockMvcRequestPostProcessors.user(new PartnersDetails(mockPartners)))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                    )

                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("성공"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.orderId").value(1L))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.memberName").value("John Doe"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.address.roadNameAddress").value("서울시 강남구"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.address.addressDetails").value("아파트 101호"));
        }
    }
}
