package com.d129cm.batchapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.d129cm.batchapi", "com.d129cm.backendapi"})
public class BatchApiApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BatchApiApplication.class);
        app.setAdditionalProfiles("batch");
        app.run(args);
    }

}
