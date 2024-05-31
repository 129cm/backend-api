package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.auth.config.CustomSecurityConfig;
import com.d129cm.backendapi.common.dto.AddressRequest;
import com.d129cm.backendapi.member.dto.MemberSignupRequest;
import com.d129cm.backendapi.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@Import(CustomSecurityConfig.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Nested
    class signup {
        @Test
        void 성공반환_멤버_회원가입_요청() throws Exception {
            // given
            AddressRequest addressRequest = new AddressRequest("1234", "Seoul", "Seoul");
            MemberSignupRequest request = new MemberSignupRequest("test@naver.com", "Asdf1234!", "이름", addressRequest);
            doNothing().when(memberService).saveMember(request);

            // when
            ResultActions result = mockMvc.perform(post("/members/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(new ObjectMapper().writeValueAsString(request)));

            // then
            result.andExpect(status().isOk())
                    .andExpect(content().json("{\"status\":200, \"message\":\"성공\"}"));
        }
    }
}
