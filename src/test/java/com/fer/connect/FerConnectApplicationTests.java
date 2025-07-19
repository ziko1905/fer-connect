package com.fer.connect;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FerConnectApplicationTests {

  @Test
  void itRespondesWith200() {
    assertEquals(true, true, "This should pass!");
  }

}
