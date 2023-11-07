package org.penekhun.wanted2023.global.rest_controller;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.penekhun.wanted2023.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

  private final int code;
  private final HttpStatus status;
  private final String message;
  private final T data;

  public ApiResponse(@NotNull ExceptionCode e) {
    this.code = e.getCode();
    this.status = e.getHttpStatus();
    this.message = e.getMessage();
    this.data = null;
  }

  public ApiResponse(HttpStatus status, String message, T data) {
    this.code = status.value();
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
    return new ApiResponse<>(httpStatus, httpStatus.name(), data);
  }

  public static <T> ApiResponse<T> ok(T data) {
    return of(HttpStatus.OK, data);
  }
}
