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

class UserBuilder {
  private String firstName = "Default";
  private String lastName = "User";
  private String email = "default@email.com";
  private String password = "LongPassword1";

  public UserBuilder withPassword(String password) {
    this.password = password;

    return this;
  }

  public User build() {
    return new User(firstName, lastName, email, password);
  }
}

class UserJSONCreator {
  static String createUserJSON(String firstName, String lastName, String email, String password) {
    return String.format("""
        {
            "firstName": "%s",
            "lastName": "%s",
            "email": "%s",
            "password": "%s"
        }
        """, firstName, lastName, email, password);
  }

  static String createUserJSON(User user) {
    return createUserJSON(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
  }
}

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
    String validUserJSON = UserJSONCreator.createUserJSON(new UserBuilder().build());

    mockMvc
        .perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(validUserJSON))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  void returnsBadRequestStatus_whenNeccessaryFieldsAreMissing() throws Exception {
    String invalidUserJSON = """
        {
          "lastName": "Čehulić",
          "email": "randomemail@gmail.com",
          "password": "Password1"
        }
        """;

    mockMvc
        .perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(invalidUserJSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void returnsBadRequestStatus_whenNeccessaryFieldsAreEmpty() throws Exception {
    String invalidUserJSON = UserJSONCreator.createUserJSON(new UserBuilder().withPassword("").build());
    mockMvc
        .perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(invalidUserJSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void returnsBadRequest_whenPasswordIsTooShort() throws Exception {
    String shortPassUserJSON = UserJSONCreator.createUserJSON(new UserBuilder().withPassword("Short").build());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(shortPassUserJSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  void reurnsBadRequestWithMessage_whenEmailAlreadyInDB() throws Exception {
    String defaultUserJSON = UserJSONCreator.createUserJSON(new UserBuilder().build());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(defaultUserJSON));

    MvcResult result = mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(defaultUserJSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

    String json = result.getResponse().getContentAsString();

    assertTrue(json.contains("An user with that email already exists"));
  }
}
