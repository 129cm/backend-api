package com.d129cm.backendapi.partners.controller;

import com.d129cm.backendapi.auth.domain.PartnersDetails;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.config.TestSecurityConfig;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.domain.SortCondition;
import com.d129cm.backendapi.item.dto.ItemCreateRequest;
import com.d129cm.backendapi.item.dto.ItemOptionCreateRequest;
import com.d129cm.backendapi.item.service.ItemService;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.*;
import com.d129cm.backendapi.partners.service.PartnersItemService;
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

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartnersItemController.class)
@Import(TestSecurityConfig.class)
public class PartnersItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private PartnersItemService partnersItemService;

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
            ResultActions result = mockMvc.perform(get("/partners/brands/items")
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

    @Nested
    class getItemDetailsForPartners {

        @Test
        void 성공200_파트너스_아이템_상세조회() throws Exception {
            // given
            Item mockItem = Item.builder()
                    .name("Item 1")
                    .price(10000)
                    .description("Item Description")
                    .image("item.png").build();

            ItemOption mockOption = ItemOption.builder()
                    .name("Option 1")
                    .optionPrice(1000)
                    .quantity(100)
                    .build();
            mockItem.addItemOption(mockOption);

            Password password = mock(Password.class);
            Partners mockPartners = spy(Partners.builder()
                    .email("testPartners@email.com")
                    .password(password)
                    .businessNumber("123-45-67890")
                    .build());

            GetItemDetailsResponse response = GetItemDetailsResponse.of(mockItem);

            doReturn(1L).when(mockPartners).getId();
            when(password.getPassword()).thenReturn("asdf123!");
            when(partnersItemService.getItemDetails(any(Long.class), any(Long.class))).thenReturn(response);
            // when
            ResultActions mvcResult = mockMvc.perform(get("/partners/brands/items/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(new PartnersDetails(mockPartners))));

            // then
            mvcResult.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.itemName").value("Item 1"))
                    .andReturn();

            verify(partnersItemService, times(1)).getItemDetails(anyLong(), anyLong());
        }
    }

    @Nested
    class putItemDetails {

        @Test
        void 성공200_파트너스_아이템_정보_수정() throws Exception {
            // given
            PutItemOptionRequest optionRequest1 = new PutItemOptionRequest(1L, "Small", 10, 0);
            PutItemOptionRequest optionRequest2 = new PutItemOptionRequest(2L, "Large", 10, 1000);
            List<PutItemOptionRequest> options = List.of(optionRequest1, optionRequest2);
            PutItemDetailsRequest request = new PutItemDetailsRequest("옷", 10000, "옷.png", "설명", options);

            Password password = mock(Password.class);
            Partners mockPartners = spy(Partners.builder()
                    .email("testPartners@email.com")
                    .password(password)
                    .businessNumber("123-45-67890")
                    .build());

            doReturn(1L).when(mockPartners).getId();
            when(password.getPassword()).thenReturn("asdf123!");

            // when
            ResultActions mvcResult = mockMvc.perform(put("/partners/brands/items/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(request))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(new PartnersDetails(mockPartners))));

            // then
            mvcResult.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));

            verify(partnersItemService, times(1)).putItemDetails(eq(1L), eq(1L), any(PutItemDetailsRequest.class));
        }
    }

    @Nested
    class deleteItem{

        @Test
        void 성공200_아이템_삭제() throws Exception {
            // given
            Long itemId = 1L;

            Password password = mock(Password.class);
            Partners mockPartners = spy(Partners.builder()
                    .email("testPartners@email.com")
                    .password(password)
                    .businessNumber("123-45-67890")
                    .build());

            doReturn(1L).when(mockPartners).getId();
            when(password.getPassword()).thenReturn("asdf123!");

            // when
            ResultActions mvcResult = mockMvc.perform(delete("/partners/brands/items/" + itemId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(SecurityMockMvcRequestPostProcessors.user(new PartnersDetails(mockPartners))));

            // then
            mvcResult.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));

            verify(partnersItemService).deleteItem(eq(1L), eq(itemId));
        }
    }

}
