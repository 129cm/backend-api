package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.common.dto.AddressRequest;
import com.d129cm.backendapi.common.dto.AddressResponse;
import com.d129cm.backendapi.config.TestSecurityConfig;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.MemberMyPageResponse;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
@Import(TestSecurityConfig.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private PasswordEncoder passwordEncoder;

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
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));
        }
    }

    @Nested
    class getMemberMyPage {

        @Test
        @WithMockUser(username = "testUser", roles = {"MEMBER"})
        void 성공반환_멤버_마이페이지_요청() throws Exception {
            // given
            Member member = Member.builder()
                    .email("test@naver.com")
                    .password(Password.of("Asdf1234!", passwordEncoder))
                    .name("이름")
                    .address(new Address("1234", "Seoul", "Seoul"))
                    .build();
            MemberDetails memberDetails = new MemberDetails(member);
            MemberMyPageResponse response = new MemberMyPageResponse("test@naver.com", "이름", new AddressResponse("1234", "Seoul", "Seoul"));

            when(memberService.getMemberMyPage(any(MemberDetails.class))).thenReturn(response);

            // when
            ResultActions result = mockMvc.perform(get("/members")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .with(user(memberDetails)));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.email").value("test@naver.com"))
                    .andExpect(jsonPath("$.data.name").value("이름"))
                    .andExpect(jsonPath("$.data.address.zipCode").value("1234"))
                    .andExpect(jsonPath("$.data.address.roadNameAddress").value("Seoul"))
                    .andExpect(jsonPath("$.data.address.addressDetails").value("Seoul"));
        }
    }
}
