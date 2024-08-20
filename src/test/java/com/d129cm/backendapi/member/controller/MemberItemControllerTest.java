package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.config.TestSecurityConfig;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.member.dto.ItemForMemberResponse;
import com.d129cm.backendapi.member.service.MemberItemService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberItemController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class MemberItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberItemService memberItemService;

    @Nested
    class getItem {

        @Test
        @WithMockUser
        void 성공200_특정_상품_조회() throws Exception {
            // given
            Long itemId = 1L;

            Brand brand = mock(Brand.class);
            Item item = mock(Item.class);
            ItemOption itemOption1 = mock(ItemOption.class);
            ItemOption itemOption2 = mock(ItemOption.class);

            ItemForMemberResponse mockResponse = ItemForMemberResponse.of
                    (brand, item, Arrays.asList(itemOption1, itemOption2));


            when(memberItemService.getItemForMember(itemId)).thenReturn(mockResponse);

            // when
            ResultActions result = mockMvc.perform(get("/members/items/{itemId}", itemId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));
        }
    }
}
