package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.manager.BrandManager;
import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.cart.manager.ItemCartManager;
import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.dto.AddressRequest;
import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.domain.SortCondition;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.*;
import com.d129cm.backendapi.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BrandManager brandManager;

    @Mock
    private ItemManager itemManager;

    @Mock
    private ItemOptionManager itemOptionManager;

    @Mock
    private ItemCartManager itemCartManager;

    @Nested
    class saveMember {
        @Test
        void 성공반환_멤버_저장() {
            // given
            AddressRequest addressRequest = new AddressRequest("1234", "Seoul", "Seoul");
            MemberSignupRequest request = new MemberSignupRequest(
                    "test@naver.com", "Asdf1234!", "이름", addressRequest);

            when(memberRepository.save(any(Member.class))).thenReturn(mock(Member.class));

            // when
            memberService.saveMember(request);

            // then
            verify(memberRepository).save(any(Member.class));
        }

        @Test
        void 예외반환_중복된_멤버() {
            // given
            AddressRequest addressRequest = new AddressRequest("1234", "Seoul", "Seoul");
            MemberSignupRequest request = new MemberSignupRequest(
                    "test@naver.com", "Asdf1234!", "이름", addressRequest);

            // when
            when(memberRepository.existsByEmail(request.email())).thenReturn(true);

            // then
            assertThrows(ConflictException.class, () -> memberService.saveMember(request));
        }
    }

    @Nested
    class getMemberMyPage {
        @Test
        void MemberMyPageResponse반환_멤버_마이페이지_조회() {
            // given
            Long memberId = 1L;
            MemberDetails memberDetails = Mockito.mock(MemberDetails.class);
            Member member = Mockito.mock(Member.class);
            Address address = Mockito.mock(Address.class);

            when(memberDetails.member()).thenReturn(member);
            when(member.getId()).thenReturn(memberId);
            when(member.getEmail()).thenReturn("test@naver.com");
            when(member.getName()).thenReturn("이름");
            when(member.getAddress()).thenReturn(address);
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

            when(address.getZipCode()).thenReturn("1234");
            when(address.getRoadNameAddress()).thenReturn("Seoul");
            when(address.getAddressDetails()).thenReturn("Seoul");

            // when
            MemberMyPageResponse response = memberService.getMemberMyPage(memberDetails);

            // then
            assertThat(response).isNotNull();
            assertThat(response.email()).isEqualTo("test@naver.com");
            assertThat(response.name()).isEqualTo("이름");
            assertThat(response.address()).isNotNull();
            assertThat(response.address().zipCode()).isEqualTo("1234");
            assertThat(response.address().roadNameAddress()).isEqualTo("Seoul");
            assertThat(response.address().addressDetails()).isEqualTo("Seoul");
        }

        @Test
        void 예외반환_멤버가_없는_경우() {
            // given
            MemberDetails memberDetails = mock(MemberDetails.class);
            Member member = mock(Member.class);

            when(memberDetails.member()).thenReturn(member);
            when(member.getId()).thenReturn(1L);
            when(memberRepository.findById(1L)).thenReturn(Optional.empty());

            // when & then
            Exception exception = assertThrows(EntityNotFoundException.class, ()
                    -> memberService.getMemberMyPage(memberDetails));
            assertEquals("일치하는 멤버가 없습니다.", exception.getMessage());
        }
    }

    @Nested
    class getBrands {
        @Test
        void BrandsForMemberResponse반환_멤버_브랜드_조회() {
            // given
            Long brandId = 1L;
            SortCondition sort = SortCondition.NEW;
            Sort.Direction sortOrder = Sort.Direction.DESC;

            Brand mockBrand = mock(Brand.class);
            when(mockBrand.getName()).thenReturn("브랜드 이름");
            when(mockBrand.getImage()).thenReturn("브랜드 이미지");
            when(mockBrand.getDescription()).thenReturn("브랜드 설명");

            List<Item> mockItems = List.of(mock(Item.class), mock(Item.class));
            Page<Item> mockItemPage = new PageImpl<>(mockItems);

            when(brandManager.getBrand(brandId)).thenReturn(mockBrand);
            when(itemManager.createItemSort(sort, sortOrder)).thenReturn(mock(Sort.class));
            when(itemManager.getAllItemByBrandId(eq(brandId), any(Pageable.class))).thenReturn(mockItemPage);

            // when
            BrandsForMemberResponse response = memberService.getBrandsForMember(sort, sortOrder, brandId, 0, 50);

            // then
            verify(brandManager).getBrand(brandId);
            verify(itemManager).createItemSort(sort, sortOrder);
            verify(itemManager).getAllItemByBrandId(eq(brandId), any(Pageable.class));

            assertThat(response).isNotNull();
            assertThat(response.brandName()).isEqualTo("브랜드 이름");
            assertThat(response.brandImage()).isEqualTo("브랜드 이미지");
            assertThat(response.brandDescription()).isEqualTo("브랜드 설명");
            assertThat(response.itemResponse()).hasSize(2);
        }
    }

    @Nested
    class getItem {
        @Test
        void ItemForMemberRespons반환_멤버_아이템_조회() {
            // given
            Long itemId = 1L;
            Item item = mock(Item.class);
            Brand brand = mock(Brand.class);
            ItemOption itemOption1 = mock(ItemOption.class);
            ItemOption itemOption2 = mock(ItemOption.class);
            List<ItemOption> itemOptions = List.of(itemOption1, itemOption2);

            when(item.getId()).thenReturn(1L);
            when(item.getName()).thenReturn("아이템명");
            when(item.getPrice()).thenReturn(1000);
            when(item.getImage()).thenReturn("아이템 이미지");

            when(itemManager.getItem(itemId)).thenReturn(item);
            when(item.getBrand()).thenReturn(brand);
            when(item.getItemOptions()).thenReturn(itemOptions);

            // when
            ItemForMemberResponse response = memberService.getItemForMember(itemId);

            // then
            verify(itemManager).getItem(itemId);
            verify(item).getBrand();
            verify(item).getItemOptions();

            assertThat(response).isNotNull();
            assertThat(response.itemId()).isEqualTo(1L);
            assertThat(response.itemName()).isEqualTo("아이템명");
            assertThat(response.itemPrice()).isEqualTo(1000);
            assertThat(response.itemImage()).isEqualTo("아이템 이미지");
            assertThat(response.itemOptionResponse()).hasSize(2);
        }
    }

    @Nested
    class addItemToCart{
        @Test
        void 성공_아이템을_장바구니에_추가() {
            // given
            Member member = Mockito.mock(Member.class);
            Cart cart = Mockito.mock(Cart.class);
            when(member.getCart()).thenReturn(cart);

            Item item = Mockito.mock(Item.class);
            ItemOption itemOption = Mockito.mock(ItemOption.class);
            CartItemRequest request = new CartItemRequest(1L, 2L, 3);

            when(itemManager.getItem(request.itemId())).thenReturn(item);
            when(itemOptionManager.getItemOption(request.itemOptionId())).thenReturn(itemOption);

            // when
            memberService.addItemToCart(member, request);

            // then
            verify(itemManager).getItem(request.itemId());
            verify(itemOptionManager).getItemOption(request.itemOptionId());
            verify(itemCartManager).createItemCart(any(ItemCart.class));
        }

        @Test
        void 예외반환_수량이_음수일_때() {
            // given
            Member member = Mockito.mock(Member.class);
            CartItemRequest request = new CartItemRequest(1L, 2L, -1);

            // when & then
            BadRequestException e = BadRequestException.negativeQuantityLimit();
            Assertions.assertThatThrownBy(() -> memberService.addItemToCart(member, request))
                    .isInstanceOf(e.getClass()).hasMessage(e.getMessage());
        }


        @Test
        void 예외반환_수량이_100을_초과할_때() {
            // given
            Member member = Mockito.mock(Member.class);
            CartItemRequest request = new CartItemRequest(1L, 2L, 101);

            // when & then
            BadRequestException e = BadRequestException.exceedQuantityLimit(100);
            Assertions.assertThatThrownBy(() -> memberService.addItemToCart(member, request))
                    .isInstanceOf(e.getClass()).hasMessage(e.getMessage());
        }
    }
}
