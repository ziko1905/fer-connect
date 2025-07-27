package com.fer.connect.config;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class TestContainerConfig {
  @Container
  public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
      .withReuse(true);
}
