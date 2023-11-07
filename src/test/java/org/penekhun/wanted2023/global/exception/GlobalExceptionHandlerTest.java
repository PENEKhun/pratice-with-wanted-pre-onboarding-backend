package org.penekhun.wanted2023.global.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GlobalExceptionHandlerTest {

  GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

  @Test
  void handleException() {
    var actualErr = ExceptionCode.INVALID_REQUEST;
    var expect =
        globalExceptionHandler.handleException(new CustomException(actualErr));

    assertEquals(expect.getStatusCode(), actualErr.getHttpStatus());
    assertEquals(expect.getBody().getCode(), actualErr.getCode());
    assertEquals(expect.getBody().getMessage(), actualErr.getMessage());
  }
}