package com.fer.connect.auth;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.HashMap;
import java.util.Map;

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
import com.fer.connect.user.User;
import com.fer.connect.user.UserRepository;
import com.fer.connect.user.util.UserBuilder;

@ActiveProfiles("integration")
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfig.class)
@Transactional
public class AuthControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  UserRepository userRepository;

  @Test
  void returnsLoginJson_whenUserAuthenticateWithRightData() throws Exception {
    User defaultUser = new UserBuilder().build();
    String defaultUserJson = objectMapper.writeValueAsString(defaultUser);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
        .content(defaultUserJson)).andExpect(MockMvcResultMatchers.status().isCreated());
    User defaultUserInDb = userRepository.findByEmail(defaultUser.getEmail());

    Map<String, String> loginJsonMap = new HashMap<>();
    loginJsonMap.put("email", defaultUser.getEmail());
    loginJsonMap.put("password", defaultUser.getPassword());
    String loginJsonString = objectMapper.writeValueAsString(loginJsonMap);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login").contentType(MediaType.APPLICATION_JSON)
        .content(loginJsonString))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.token").value("someToken"))
        .andExpect(jsonPath("$.user.firstName").value(defaultUserInDb.getFirstName()))
        .andExpect(jsonPath("$.user.lastName").value(defaultUserInDb.getLastName()))
        .andExpect(jsonPath("$.user.email").value(defaultUserInDb.getEmail()))
        .andExpect(jsonPath("$.user.id").value(defaultUserInDb.getId().toString()));
  }
}
