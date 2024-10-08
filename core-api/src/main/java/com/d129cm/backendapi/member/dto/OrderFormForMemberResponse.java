package com.d129cm.backendapi.member.dto;

import java.util.List;

public record OrderFormForMemberResponse (
        String userName,
        AddressResponse address,
        List<BrandsForOrderResponse> brandsForOrderResponse
){
}
