package org.penekhun.wanted2023.global.exception;

import org.penekhun.wanted2023.global.rest_controller.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<Void>> handleException(final CustomException e) {
    ExceptionCode err = e.getExceptionCode();
    return ResponseEntity
        .status(err.getHttpStatus())
        .body(
            new ApiResponse<>(
                err.getHttpStatus(),
                err.getMessage(),
                null)
        );
  }

}
