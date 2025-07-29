package com.fer.connect.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fer.connect.user.exception.UserWithSameEmailException;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void save(User newUser) {
    User existingUser = userRepository.findByEmail(newUser.getEmail());

    if (existingUser != null) {
      throw new UserWithSameEmailException();
    }

    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

    userRepository.save(newUser);
  }
}
