package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.config.TestSecurityConfig;
import com.d129cm.backendapi.item.domain.SortCondition;
import com.d129cm.backendapi.member.dto.BrandsForMemberResponse;
import com.d129cm.backendapi.member.dto.ItemResponse;
import com.d129cm.backendapi.member.service.MemberService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberBrandController.class)
@Import(TestSecurityConfig.class)
@SuppressWarnings("NonAsciiCharacters")
@AutoConfigureMockMvc(addFilters = false)
public class MemberBrandControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @Nested
    class getBrand {

        @Test
        @WithAnonymousUser
        void 성공200_브랜드_조회() throws Exception {
            // given
            SortCondition sort = SortCondition.NEW;
            Sort.Direction sortOrder = Sort.Direction.DESC;
            Long brandId = 1L;
            int page = 0;
            int size = 50;

            BrandsForMemberResponse mockResponse = new BrandsForMemberResponse(
                    "BrandName",
                    "BrandImage",
                    "BrandDescription",
                    List.of(new ItemResponse(1L, "ItemName", 100, "ItemImage"))
            );

            when(memberService.getBrandsForMember(sort, sortOrder, brandId, page, size)).thenReturn(mockResponse);

            // when
            ResultActions result = mockMvc.perform(get("/members/brands/{brandId}", brandId)
                    .param("sort", sort.name())
                    .param("sortOrder", sortOrder.name())
                    .param("page", String.valueOf(page))
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));

        }
    }
}
