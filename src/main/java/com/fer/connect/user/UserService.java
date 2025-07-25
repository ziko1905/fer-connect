package com.fer.connect.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void save(User newUser) throws Exception {
    User oldU = userRepository.findByEmail(newUser.getEmail());

    if (oldU != null) {
      throw new Exception();
    }

    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

    userRepository.save(newUser);
  }
}
