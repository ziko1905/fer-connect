package com.fer.connect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fer.connect.config.TestContainerConfig;
import com.fer.connect.exception.IntendedErrorResponse;
import com.fer.connect.exception.RestErrorType;
import com.fer.connect.user.util.UserBuilder;
import com.fer.connect.user.util.UserJsonWriter;

@ActiveProfiles("integration")
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfig.class)
@Transactional
class FerConnectApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void haveRightJsonErrorFormat_whenIntendedAPIErrorOccurs() throws Exception {
    String sameEmailUserJson = UserJsonWriter.writeString(new UserBuilder().build());

    // Makes sure email is in the db so that exception api will respond with error
    mockMvc.perform(
        MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
            .content(sameEmailUserJson));

    MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(sameEmailUserJson))
        .andExpect(MockMvcResultMatchers.status().is(RestErrorType.USER_EMAIL_EXISTS.getHttpStatusCode()))
        .andExpect(jsonPath("$.status").value(RestErrorType.USER_EMAIL_EXISTS.getHttpStatusCode()))
        .andExpect(jsonPath("$.path").value("/api/v1/users"))
        .andExpect(jsonPath("$.error.code").value(RestErrorType.USER_EMAIL_EXISTS.getErrorCode()))
        .andExpect(jsonPath("$.error.message").value(RestErrorType.USER_EMAIL_EXISTS.getMessage())).andReturn();

    IntendedErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
        IntendedErrorResponse.class);

    Instant responseTimestamp = errorResponse.getTimestamp();
    Instant higherTimestampLimit = Instant.now();
    Instant lowerTimestampLimit = higherTimestampLimit.minusSeconds(5);

    assertThat(responseTimestamp).isBetween(lowerTimestampLimit, higherTimestampLimit);
  }
}
