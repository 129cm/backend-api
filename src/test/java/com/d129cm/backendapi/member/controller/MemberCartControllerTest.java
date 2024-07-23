package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.config.TestSecurityConfig;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.CartItemRequest;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberCartController.class)
@Import(TestSecurityConfig.class)
@SuppressWarnings("NonAsciiCharacters")
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
            Member mockMember = spy(Member.builder()
                    .email("test@email.com")
                    .password(password)
                    .name("이름")
                    .address(mock(Address.class))
                    .build());
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
}
