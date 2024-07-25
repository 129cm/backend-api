package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.partners.domain.Partners;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

public class Fixture {

    public Member createMember() {
        Address address = spy(new Address("서울시", "주소구", "03000"));
        Password password = spy(createPassword());
        Member member = spy(
                Member.builder()
                        .email("user@example.com")
                        .password(password)
                        .name("이름")
                        .address(address)
                        .build());
        return member;
    }

    public Cart createCart(Member member) {
        Cart cart = spy(new Cart());
        member.setCart(cart);
        return cart;
    }

    public ItemCart createItemCart(Item item, ItemOption itemOption) {
        Cart cart = createCart(createMember());
        ItemCart itemCart = spy(ItemCart.builder()
                .count(1)
                .item(item)
                .itemOption(itemOption)
                .cart(cart  )
                .build());
        return itemCart;
    }

    public Item createItem(Brand brand) {
        Item item = spy(Item.builder()
                .name("상품 이름")
                .price(1000)
                .image("상품 이미지")
                .description("상품 설명")
                .build());
        brand.addItem(item);
        when(item.getId()).thenReturn(4L);
        return item;
    }

    public ItemOption createItemOption(Item item) {
        ItemOption itemOption = spy(ItemOption.builder()
                .name("상품 옵션 이름")
                .quantity(100)
                .optionPrice(200)
                .build());
        item.addItemOption(itemOption);
        when(itemOption.getId()).thenReturn(5L);
        when(itemOption.getOptionPrice()).thenReturn(200);
        return itemOption;
    }

    public Brand createBrand() {
        Partners partners = createPartners();
        Brand brand = spy(Brand.builder()
                .name("브랜드 이름")
                .description("브랜드 설명")
                .image("브랜드 이미지")
                .partners(partners)
                .build());
        when(brand.getId()).thenReturn(6L);
        return brand;
    }

    public Partners createPartners() {
        Password password = createPassword();
        Partners partners = spy(Partners.builder()
                .email("partners@example.com")
                .businessNumber("123-12-12345")
                .password(password)
                .build());
        return partners;
    }

    public Password createPassword() {
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        return Password.of(rawPassword, passwordEncoder);
    }
}
