package com.fer.connect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class FerConnectApplication {

  public static void main(String[] args) {
    SpringApplication.run(FerConnectApplication.class, args);
  }
}
