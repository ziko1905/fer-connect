package com.fer.connect.user.exception;

import com.fer.connect.exception.RestException;
import com.fer.connect.exception.RestErrorType;

public class UserWithSameEmailException extends RestException {
  public static final RestErrorType ERROR_TYPE = RestErrorType.USER_EMAIL_EXISTS;

  public UserWithSameEmailException() {
    super(ERROR_TYPE);
  }
}
