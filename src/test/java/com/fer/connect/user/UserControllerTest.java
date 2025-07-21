package com.fer.connect.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void itRespondesWithOkOnRightRequest() throws Exception {
    String validUserJson = """
        {
          "firstName": "Karlo",
          "lastName": "Čehulić",
          "email": "randomemail@gmail.com",
          "password": "Strong1"
        }
        """;

    mockMvc
        .perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(validUserJson))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }
}
