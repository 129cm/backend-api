package com.d129cm.backendapi.payment.controller;

import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.payment.dto.PaymentResultDto;
import com.d129cm.backendapi.payment.service.PaymentService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final RestTemplate restTemplate;
    private final Base64.Encoder encoder = Base64.getEncoder();

    @Value("${payment.toss.test_secret_api_key}")
    private String widgetSecretKey;

    @PostMapping("/confirm")
    public ResponseEntity<PaymentResultDto> confirmPayment(@RequestParam String paymentKey,
                                                           @RequestParam("orderId") String tossOrderId,
                                                           @RequestParam Integer amount) throws Exception {
        Order order = paymentService.getOrderByOrderSerial(tossOrderId);
        Long orderId = order.getId();

        Integer totalPrice = paymentService.getTotalPrice(orderId);
        if (!Objects.equals(amount, totalPrice)) {
            throw BadRequestException.wrongOrder("결제 금액");
        }

        paymentService.prepareOrder(order, paymentKey);

        JsonObject obj = new JsonObject();
        obj.addProperty("orderId", tossOrderId);
        obj.addProperty("amount", amount);
        obj.addProperty("paymentKey", paymentKey);

        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizations);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(obj.toString(), headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/confirm",
                requestEntity,
                String.class
        );

        JsonObject jsonObject = JsonParser.parseString(responseEntity.getBody()).getAsJsonObject();
        String status = jsonObject.get("status").getAsString();

        if (responseEntity.getStatusCode().is2xxSuccessful() && status.equalsIgnoreCase("DONE")) {
            try {
                paymentService.completeOrder(order);
            } catch (Exception e) {
                // 결제 완료 처리 중 오류 발생 시 결제 취소 호출
                String cancelUrl = "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel";
                restTemplate.postForEntity(cancelUrl, requestEntity, String.class);

                PaymentResultDto result = PaymentResultDto.withdraw("결제 완료 처리 중 에러 발생하여 취소 API 호출을 완료했습니다.");
                return ResponseEntity.ok(result);
            }

            PaymentResultDto result = PaymentResultDto.success("결제에 성공하였습니다!");
            return ResponseEntity.ok(result);
        }

        // 결제 실패
        paymentService.undoOrder(order);
        PaymentResultDto result = PaymentResultDto.fail("결제 승인 처리가 정상적으로 완료되지 않았습니다. \nStatus: " + jsonObject.get("status").getAsString());
        return ResponseEntity.status(responseEntity.getStatusCode()).body(result);
    }
}
