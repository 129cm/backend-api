package com.d129cm.backendapi.payment;

public record PaymentResultDto (
        boolean isPaymentSuccess,
        String message
) {
    public static PaymentResultDto withdraw( String message) {
        return new PaymentResultDto(false, message);
    }

    public static PaymentResultDto success(String message) {
        return new PaymentResultDto(true, message);
    }

    public static PaymentResultDto fail(String message) {
        return new PaymentResultDto(false, message);
    }
}
