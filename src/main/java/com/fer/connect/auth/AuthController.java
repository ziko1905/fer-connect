package com.fer.connect.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fer.connect.auth.dto.LoginUserDto;
import com.fer.connect.user.User;
import com.fer.connect.user.UserRepository;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  UserRepository userRepository;

  @PostMapping("/login")
  public ResponseEntity<?> loginPost(@RequestBody LoginUserDto loginUserDto) throws JsonProcessingException {
    Map<String, Object> loginJsonMapping = new HashMap<>();
    System.out.println(loginUserDto.getEmail());

    User user = userRepository.findByEmail(loginUserDto.getEmail());

    loginJsonMapping.put("token", "someToken");
    loginJsonMapping.put("user", user);

    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(objectMapper.writeValueAsString(loginJsonMapping));
  }

}
