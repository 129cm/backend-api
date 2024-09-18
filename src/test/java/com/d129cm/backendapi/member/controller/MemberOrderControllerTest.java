package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.config.TestSecurityConfig;
import com.d129cm.backendapi.fixture.MemberFixture;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.AddressResponse;
import com.d129cm.backendapi.member.dto.BrandsForOrderResponse;
import com.d129cm.backendapi.member.dto.ItemWithOptionForOrderResponse;
import com.d129cm.backendapi.member.dto.OrderFormForMemberResponse;
import com.d129cm.backendapi.member.service.MemberOrderService;
import com.d129cm.backendapi.order.dto.CreateOrderDto;
import com.d129cm.backendapi.order.dto.OrderFormDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberOrderController.class)
@Import(TestSecurityConfig.class)
public class MemberOrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberOrderService memberOrderService;

    @Nested
    class getOrderForm {

        @Test
        @WithMockUser
        void 성공200_주문페이지_요청() throws Exception {
            // given
            Member member = MemberFixture.createMember("test@email.com");

            Long itemId = 1L;
            Long itemOptionId = 2L;
            Integer count = 3;

            List<OrderFormDto> orderFormDto = new ArrayList<>();
            orderFormDto.add(new OrderFormDto(itemId, itemOptionId, count));

            AddressResponse address = mock(AddressResponse.class);
            List<BrandsForOrderResponse> brandsForOrderResponses = new ArrayList<>();

            OrderFormForMemberResponse mockResponse = new OrderFormForMemberResponse(member.getName(), address, brandsForOrderResponses);
            when(memberOrderService.getOrderForm(orderFormDto, member)).thenReturn(mockResponse);

            // when
            ResultActions result = mockMvc.perform(get("/members/orders/order-form")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(spy(new MemberDetails(member))))
                    .content(new ObjectMapper().writeValueAsString(orderFormDto)))
                            .andDo(print());


            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.userName").value("이름"))
                    .andExpect(jsonPath("$.data.address").exists())
                    .andExpect(jsonPath("$.data.brandsForOrderResponse").exists())
                    .andExpect(jsonPath("$.data.brandsForOrderResponse").isArray());
        }
    }

    @Nested
    class createOrder {

        @Test
        void 성공200_주문_생성() throws Exception {
            // given
            Member member = MemberFixture.createMember("test@email.com");

            Long brandId = 1L;
            String brandName = "브랜드 이름";

            List<ItemWithOptionForOrderResponse> itemResponse = new ArrayList<>();
            itemResponse.add(new ItemWithOptionForOrderResponse(1L, "아이템 이름", 1000, "아이템 이미지", 1L, "아이템 옵션 이름", 100, 1));
            List<BrandsForOrderResponse> brandsForOrderResponses = new ArrayList<>();
            brandsForOrderResponses.add(new BrandsForOrderResponse(brandId, brandName, itemResponse));
            CreateOrderDto createOrderDto = new CreateOrderDto(brandsForOrderResponses);

            // when
            ResultActions result = mockMvc.perform(post("/members/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(spy(new MemberDetails(member))))
                    .content(new ObjectMapper().writeValueAsString(createOrderDto)));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));
        }
    }
}
