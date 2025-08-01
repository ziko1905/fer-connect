package com.fer.connect.user.util;

import com.fer.connect.user.User;

public class UserBuilder {
  private String firstName = "Default";
  private String lastName = "User";
  private String email = "default@email.com";
  private String password = "LongPassword1";

  public UserBuilder withPassword(String password) {
    this.password = password;

    return this;
  }

  public UserBuilder withEmail(String email) {
    this.email = email;

    return this;
  }

  public User build() {
    return new User(firstName, lastName, email, password);
  }
}
