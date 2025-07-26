package com.fer.connect;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fer.connect.user.util.UserBuilder;
import com.fer.connect.user.util.UserJSONWriter;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class FerConnectApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Container
  public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Test
  void haveRightJSONErrorFormat_whenIntendedAPIErrorOccurs() throws Exception {
    // intented error formating, here is used error for user reg with email in db
    // for example purpuses
    // any other intended error(error that is needed in frontent impl) could be used
    String sameEmailUserJSON = UserJSONWriter.createUserJSON(new UserBuilder().build());

    mockMvc.perform(
        MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(sameEmailUserJSON));

    mockMvc.perform(
        MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(sameEmailUserJSON))
        .andExpect(MockMvcResultMatchers.content().json("""
            {
              \"timestamp\": \"SOMETIMESTAMP\",
              \"status\": \"400\",
              \"path\": \"/users\",
              \"error\": {
                \"code\": \"same_email\",
                \"message\": \"An intended message\"
              }
            }
            """));

  }
}
