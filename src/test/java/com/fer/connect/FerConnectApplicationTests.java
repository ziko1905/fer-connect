package com.fer.connect;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fer.connect.config.TestContainerConfig;
import com.fer.connect.user.util.UserBuilder;
import com.fer.connect.user.util.UserJsonWriter;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfig.class)
@Transactional
class FerConnectApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", TestContainerConfig.postgres::getJdbcUrl);
    registry.add("spring.datasource.username", TestContainerConfig.postgres::getUsername);
    registry.add("spring.datasource.password", TestContainerConfig.postgres::getPassword);
  }

  @Test
  void haveRightJsonErrorFormat_whenIntendedAPIErrorOccurs() throws Exception {
    // intented error formating, here is used error for user reg with email in db
    // for example purpuses
    // any other intended error(error that is needed in frontent impl) could be used
    String sameEmailUserJson = UserJsonWriter.writeString(new UserBuilder().build());

    mockMvc.perform(
        MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(sameEmailUserJson));

    mockMvc.perform(
        MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(sameEmailUserJson))
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
