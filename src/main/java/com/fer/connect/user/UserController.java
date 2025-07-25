package com.fer.connect.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;

  }

  @PostMapping("/users")
  public ResponseEntity<Object> postUser(@RequestBody @Valid User newUser) {
    try {
      userService.save(newUser);
    } catch (Exception e) {
      return new ResponseEntity<>("An user with that email already exists", HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
