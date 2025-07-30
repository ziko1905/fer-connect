package com.fer.connect.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;

  }

  @PostMapping
  public ResponseEntity<Object> postUser(@RequestBody @Valid User newUser) {
    userService.save(newUser);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
