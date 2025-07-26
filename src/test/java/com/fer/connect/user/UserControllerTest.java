package com.fer.connect.user;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fer.connect.user.util.UserBuilder;
import com.fer.connect.user.util.UserJsonWriter;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class UserControllerTest {

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
  void returnsCreatedStatus_whenUserDataIsValid() throws Exception {
    String validUserJson = UserJsonWriter.writeString(new UserBuilder().build());

    mockMvc
        .perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(validUserJson))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  void returnsBadRequestStatus_whenNeccessaryFieldsAreMissing() throws Exception {
    String invalidUserJson = """
        {
          "lastName": "Čehulić",
          "email": "randomemail@gmail.com",
          "password": "Password1"
        }
        """;

    mockMvc
        .perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(invalidUserJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void returnsBadRequestStatus_whenNeccessaryFieldsAreEmpty() throws Exception {
    String invalidUserJson = UserJsonWriter.writeString(new UserBuilder().withPassword("").build());
    mockMvc
        .perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(invalidUserJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void returnsBadRequest_whenPasswordIsTooShort() throws Exception {
    String shortPassUserJson = UserJsonWriter.writeString(new UserBuilder().withPassword("Short").build());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(shortPassUserJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  void reurnsBadRequestWithMessage_whenEmailAlreadyInDB() throws Exception {
    String defaultUserJson = UserJsonWriter.writeString(new UserBuilder().build());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(defaultUserJson));

    MvcResult result = mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(defaultUserJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

    String responseContent = result.getResolvedException().getMessage();

    System.out.println("HERE IS THE Json: " + responseContent + " AH");
    assertTrue(responseContent.contains("An user with that email already exists"));
  }
}
