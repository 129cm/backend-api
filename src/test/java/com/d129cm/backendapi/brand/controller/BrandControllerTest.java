package com.d129cm.backendapi.brand.controller;

import com.d129cm.backendapi.auth.domain.PartnersDetails;
import com.d129cm.backendapi.brand.dto.BrandCreateRequest;
import com.d129cm.backendapi.brand.service.BrandService;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.config.TestSecurityConfig;
import com.d129cm.backendapi.partners.domain.Partners;
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

@WebMvcTest(BrandController.class)
@Import(TestSecurityConfig.class)
public class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService brandService;

    @Nested
    class createBrand {

        @Test
        void 성공200_브랜드_생성() throws Exception {
            // given
            BrandCreateRequest request = new BrandCreateRequest("brandName", "brandDescription", "brandImage");
            Password password = mock(Password.class);
            Partners mockPartners = spy(Partners.builder()
                    .email("testPartners@email.com")
                    .password(password))
                    .businessNumber("123-45-67890")
                    .build();
            when(password.getPassword()).thenReturn("asdf123!");
            doNothing().when(brandService).createBrand(mockPartners, request);

            // when
            ResultActions result = mockMvc.perform(post("/partners/brands")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(spy(new PartnersDetails(mockPartners))))
                    .content(new ObjectMapper().writeValueAsString(request)));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));
        }
    }

}
