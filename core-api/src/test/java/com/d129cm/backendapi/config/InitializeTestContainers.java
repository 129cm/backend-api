package com.d129cm.backendapi.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public interface InitializeTestContainers {
    @Container
    @ServiceConnection
    MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.37");
}