package com.d129cm.backendapi.payment.controller;

import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.service.OrderService;
import com.d129cm.backendapi.payment.dto.PaymentResultDto;
import com.d129cm.backendapi.payment.service.PaymentService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final Base64.Encoder encoder = Base64.getEncoder();

    @Value("${payment.toss.test_secret_api_key}")
    private String widgetSecretKey;

    @PostMapping("/confirm")
    public ResponseEntity<PaymentResultDto> confirmPayment(@RequestParam String paymentKey, @RequestParam("orderId") String tossOrderId, @RequestParam Integer amount) throws Exception {
        Order order = paymentService.getOrderByOrderSerial(tossOrderId);
        Long orderId = order.getId();

        Integer totalPrice = paymentService.getTotalPrice(orderId);
        if (!Objects.equals(amount, totalPrice)) {
            throw BadRequestException.wrongOrder("결제 금액");
        }

        paymentService.prepareOrder(order, tossOrderId, paymentKey);

        JsonObject obj = new JsonObject();
        obj.addProperty("orderId", tossOrderId);
        obj.addProperty("amount", amount);
        obj.addProperty("paymentKey", paymentKey);

        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes);

        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        InputStreamReader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        responseStream.close();

        String status = jsonObject.get("status").getAsString();

        // 결제 성공
        if (isSuccess && status.equalsIgnoreCase("DONE")) {
            try {
                paymentService.completeOrder(order);
            } catch (Exception e) {
                // 서버에 문제가 생긴 경우
                String cancelUrlString = "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel";
                URL cancelUrl = new URL(cancelUrlString);
                HttpURLConnection cancelConnection = (HttpURLConnection) cancelUrl.openConnection();
                cancelConnection.setRequestProperty("Authorization", authorizations);
                cancelConnection.setRequestProperty("Content-Type", "application/json");
                cancelConnection.setRequestMethod("POST");
                cancelConnection.setDoOutput(true);

                OutputStream cancelOutputStream = connection.getOutputStream();
                cancelOutputStream.write(obj.toString().getBytes("UTF-8"));

                PaymentResultDto result = PaymentResultDto.withdraw("결제 완료 처리 중 에러 발생하여 취소 API 호출을 완료했습니다.");

                return ResponseEntity.ok(result);
            }

            PaymentResultDto result = PaymentResultDto.success("결제에 성공하였습니다!");
            return ResponseEntity.ok(result);
        }

        // 결제 실패
        paymentService.undoOrder(order);
        PaymentResultDto result = PaymentResultDto.fail("결제 승인 처리가 정상적으로 완료되지 않았습니다. \nStatus: " + jsonObject.get("status").getAsString());
        return ResponseEntity.status(code).body(result);
    }
}
