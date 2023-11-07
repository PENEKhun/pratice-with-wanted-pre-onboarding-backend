package org.penekhun.wanted2023.global.exception;

import org.penekhun.wanted2023.global.rest_controller.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<Void>> handleException(final CustomException e) {
    ExceptionCode err = e.getExceptionCode();
    return ResponseEntity
        .status(err.getHttpStatus())
        .body(new ApiResponse<>(err)
        );
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  public ApiResponse<?> handleBindException(BindException e) {
    ObjectError firstErr = e.getBindingResult().getAllErrors().get(0);
    return ApiResponse.of(HttpStatus.BAD_REQUEST, firstErr.getDefaultMessage());
  }

}
