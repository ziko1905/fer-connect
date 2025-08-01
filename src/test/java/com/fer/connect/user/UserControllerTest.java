package com.fer.connect.user;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fer.connect.config.TestContainerConfig;
import com.fer.connect.exception.RestErrorType;
import com.fer.connect.user.util.UserBuilder;

@ActiveProfiles("integration")
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfig.class)
@Transactional
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void returnsCreatedStatus_whenUserDataIsValid() throws Exception {
    String validUserJson = objectMapper.writeValueAsString(new UserBuilder().build());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(validUserJson))
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
        .perform(MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
            .content(invalidUserJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void returnsBadRequestStatus_whenNeccessaryFieldsAreEmpty() throws Exception {
    String invalidUserJson = objectMapper.writeValueAsString(new UserBuilder().withPassword("").build());
    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
            .content(invalidUserJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void returnsBadRequest_whenPasswordIsTooShort() throws Exception {
    String shortPassUserJson = objectMapper.writeValueAsString(new UserBuilder().withPassword("Short").build());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(shortPassUserJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  void returnsBadRequestWithMessage_whenEmailAlreadyInDB() throws Exception {
    String defaultUserJson = objectMapper.writeValueAsString(new UserBuilder().build());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(defaultUserJson));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(defaultUserJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content()
            .string(Matchers.containsString(RestErrorType.USER_EMAIL_EXISTS.getMessage())));
  }

  @Test
  void returnsBadRequest_whenInvalidEmailFormatIsUsed() throws Exception {
    String invalidEmailUserJson = objectMapper.writeValueAsString(new UserBuilder().withEmail("notValidEmail").build());

    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
            .content(invalidEmailUserJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
