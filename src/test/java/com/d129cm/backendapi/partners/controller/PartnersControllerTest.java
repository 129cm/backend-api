package com.d129cm.backendapi.partners.controller;

import com.d129cm.backendapi.partners.dto.PartnersSignupRequest;
import com.d129cm.backendapi.partners.service.PartnersService;
import com.d129cm.backendapi.security.CustomSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(PartnersController.class)
@Import(CustomSecurityConfig.class)
public class PartnersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnersService partnersService;

    @Nested
    class signup {
        @Test
        void 성공_파트너스_회원가입_요청() throws Exception {
            // given
            PartnersSignupRequest request = new PartnersSignupRequest("email@naver.com", "password", "123-12-12312");
            doNothing().when(partnersService).savePartners(request);

            // when
            ResultActions result = mockMvc.perform(post("/partners/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(new ObjectMapper().writeValueAsString(request)));

            // then
            result.andExpect(status().isOk())
                    .andExpect(content().json("{\"status\":200, \"message\":\"성공\"}"));
        }
    }
}
