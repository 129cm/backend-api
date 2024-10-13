package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.config.TestSecurityConfig;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.CartForMemberResponse;
import com.d129cm.backendapi.member.dto.CartItemRequest;
import com.d129cm.backendapi.member.dto.CartItemUpdateRequest;
import com.d129cm.backendapi.member.service.MemberCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberCartController.class)
@Import(TestSecurityConfig.class)
public class MemberCartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberCartService memberCartService;

    @Nested
    class addItemToCart {

        @Test
        void 성공200_장바구니에_아이템_추가() throws Exception {
            // given
            Long itemId = 1L;
            Long itemOptionId = 2L;
            Integer count = 3;
            CartItemRequest request = new CartItemRequest(itemId, itemOptionId, count);

            Password password = mock(Password.class);
            Member mockMember = Member.builder()
                    .email("test@email.com")
                    .password(password)
                    .name("이름")
                    .address(mock(Address.class))
                    .build();
            when(password.getPassword()).thenReturn("asdf123!");

            doNothing().when(memberCartService).addItemToCart(mockMember, request);

            // when
            ResultActions result = mockMvc.perform(post("/members/carts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(spy(new MemberDetails(mockMember))))
                    .content(new ObjectMapper().writeValueAsString(request)));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));
        }
    }

    @Nested
    class getCart {

        @Test
        void 성공200_장바구니_아이템_조회() throws Exception {
            // given
            Password password = mock(Password.class);
            Member mockMember = Member.builder()
                    .email("test@email.com")
                    .password(password)
                    .name("이름")
                    .address(mock(Address.class))
                    .build();
            when(password.getPassword()).thenReturn("asdf123!");

            List<CartForMemberResponse> responses = new ArrayList<>();

            when(memberCartService.getCart(mockMember)).thenReturn(responses);

            // when
            ResultActions result = mockMvc.perform(get("/members/carts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(spy(new MemberDetails(mockMember)))));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));
        }
    }

    @Nested
    class updateItemQuantityInCart {

        @Test
        void 성공200_장바구니_아이템_조회() throws Exception {
            // given
            Password password = mock(Password.class);
            Member mockMember = Member.builder()
                    .email("test@email.com")
                    .password(password)
                    .name("이름")
                    .address(mock(Address.class))
                    .build();
            when(password.getPassword()).thenReturn("asdf123!");

            CartItemUpdateRequest request = new CartItemUpdateRequest(1L, 2L, 3);
            doNothing().when(memberCartService).updateItemQuantityInCart(mockMember, request);

            // when
            ResultActions result = mockMvc.perform(put("/members/carts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(spy(new MemberDetails(mockMember))))
                    .content(new ObjectMapper().writeValueAsString(request)));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));
        }
    }

    @Nested
    class deleteItemFromCart {

        @Test
        void 성공200_장바구니_아이템_삭제() throws Exception {
            // given
            Password password = mock(Password.class);
            Member mockMember = Member.builder()
                    .email("test@email.com")
                    .password(password)
                    .name("이름")
                    .address(mock(Address.class))
                    .build();
            when(password.getPassword()).thenReturn("asdf123!");

            Long itemId = 1L;
            Long itemOptionId = 2L;
            doNothing().when(memberCartService).deleteItemFromCart(mockMember, itemId, itemOptionId);

            // when
            ResultActions result = mockMvc.perform(delete("/members/carts/{itemId}/{itemOptionId}", itemId, itemOptionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(spy(new MemberDetails(mockMember)))));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));
        }
    }
}
