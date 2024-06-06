package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.repository.BrandRepository;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.common.dto.AddressResponse;
import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.repository.ItemRepository;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.BrandsForMemberResponse;
import com.d129cm.backendapi.member.dto.MemberMyPageResponse;
import com.d129cm.backendapi.member.dto.MemberSignupRequest;
import com.d129cm.backendapi.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BrandRepository brandRepository;
    private final ItemRepository itemRepository;

    public void saveMember(MemberSignupRequest request) {
        Password newPassword = Password.of(request.password(), passwordEncoder);
        if (memberRepository.existsByEmail(request.email())) {
            throw ConflictException.duplicatedValue("email", request.email());
        }

        Member newMember = Member.builder()
                .email(request.email())
                .password(newPassword)
                .name(request.name())
                .address(request.address().toAddressEntity())
                .build();
        memberRepository.save(newMember);
    }

    public MemberMyPageResponse getMemberMyPage(MemberDetails memberDetails) {
        Member member = memberRepository.findById(memberDetails.member().getId())
                .orElseThrow(()-> new EntityNotFoundException("일치하는 멤버가 없습니다."));
        AddressResponse addressResponse = AddressResponse.of(member.getAddress());
        return new MemberMyPageResponse(member.getEmail(), member.getName(), addressResponse);
    }

    public BrandsForMemberResponse getBrandsForMember(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("일치하는 브랜드가 없습니다."));
        List<Item> items = itemRepository.findAllByBrandId(brandId);
        return BrandsForMemberResponse.of(brand, items);
    }
}
