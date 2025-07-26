package com.fer.connect.user.util;

import com.fer.connect.user.User;

public class UserJsonWriter {
  public static String writeString(String firstName, String lastName, String email, String password) {
    return String.format("""
        {
            "firstName": "%s",
            "lastName": "%s",
            "email": "%s",
            "password": "%s"
        }
        """, firstName, lastName, email, password);
  }

  public static String writeString(User user) {
    return writeString(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
  }
}
