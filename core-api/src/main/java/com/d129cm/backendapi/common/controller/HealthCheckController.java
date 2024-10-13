package com.d129cm.backendapi.common.controller;

import com.d129cm.backendapi.common.dto.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/ping")
    public ResponseEntity<CommonResponse<String>> healthCheck() {
        return ResponseEntity.ok(CommonResponse.success("pong"));
    }
}
