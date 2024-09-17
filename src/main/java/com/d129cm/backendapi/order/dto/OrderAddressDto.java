package com.d129cm.backendapi.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class OrderAddressDto {
    private String zipCode;
    private String roadNameAddress;
    private String addressDetails;

    @QueryProjection
    public OrderAddressDto(String addressDetails, String roadNameAddress, String zipCode) {
        this.addressDetails = addressDetails;
        this.roadNameAddress = roadNameAddress;
        this.zipCode = zipCode;
    }
}
