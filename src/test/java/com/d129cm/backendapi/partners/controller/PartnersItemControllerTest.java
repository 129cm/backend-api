package com.d129cm.backendapi.partners.controller;

import com.d129cm.backendapi.auth.domain.PartnersDetails;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.config.TestSecurityConfig;
import com.d129cm.backendapi.item.domain.SortCondition;
import com.d129cm.backendapi.item.dto.ItemCreateRequest;
import com.d129cm.backendapi.item.dto.ItemOptionCreateRequest;
import com.d129cm.backendapi.item.service.ItemService;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.GetItemsForPartnersResponse;
import com.d129cm.backendapi.partners.dto.ItemForPartnersResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartnersItemController.class)
@Import(TestSecurityConfig.class)
@SuppressWarnings("NonAsciiCharacters")
public class PartnersItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Nested
    class createBrand {

        @Test
        void 성공200_브랜드_아이템_생성() throws Exception {
            // given
            ItemOptionCreateRequest optionRequest = new ItemOptionCreateRequest("옵션명", 24, 1000);
            List<ItemOptionCreateRequest> optionCreateRequests = List.of(optionRequest);
            ItemCreateRequest request = new ItemCreateRequest("아이템", 10000, optionCreateRequests, "아이템사진", "아이템설명");
            Password password = mock(Password.class);
            Partners mockPartners = spy(Partners.builder()
                    .email("testPartners@email.com")
                    .password(password))
                    .businessNumber("123-45-67890")
                    .build();
            when(password.getPassword()).thenReturn("asdf123!");
            doNothing().when(itemService).createItem(mockPartners, request);

            // when
            ResultActions result = mockMvc.perform(post("/partners/brands/items")
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

    @Nested
    class GetItemsForPartnersTest {

        @Test
        void 성공200_브랜드_아이템_조회() throws Exception {
            // given
            Password password = mock(Password.class);
            Partners mockPartners = spy(Partners.builder()
                    .email("testPartners@email.com")
                    .password(password))
                    .businessNumber("123-45-67890")
                    .build();
            when(password.getPassword()).thenReturn("asdf123!");
            GetItemsForPartnersResponse response = new GetItemsForPartnersResponse(List.of(
                    new ItemForPartnersResponse(1L, "Item 1", 1000, "image1.jpg", "Brand A", null),
                    new ItemForPartnersResponse(2L, "Item 2", 2000, "image2.jpg", "Brand A", null)
            ));

            when(itemService.getItemsForPartners(any(Partners.class), any(SortCondition.class), any(Sort.Direction.class), anyInt())).thenReturn(response);

            // when
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/partners/brands/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .param("sort", "NEW")
                    .param("sortOrder", "DESC")
                    .param("page", "0")
                    .with(SecurityMockMvcRequestPostProcessors.user(spy(new PartnersDetails(mockPartners)))));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.itemResponses[0].itemName").value("Item 1"))
                    .andExpect(jsonPath("$.data.itemResponses[1].itemName").value("Item 2"));
        }
    }

}
