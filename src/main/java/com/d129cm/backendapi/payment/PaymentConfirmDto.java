package com.d129cm.backendapi.payment;

public record PaymentConfirmDto (
        String paymentKey,
        String orderId,
        Integer amount
){
}
