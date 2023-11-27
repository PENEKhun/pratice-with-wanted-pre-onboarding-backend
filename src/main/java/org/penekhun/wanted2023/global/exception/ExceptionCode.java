package org.penekhun.wanted2023.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
  INVALID_REQUEST(BAD_REQUEST, 1000, "올바르지 않은 요청입니다."),

  EXPIRED_TOKEN(BAD_REQUEST, 2000, "만료된 토큰입니다.");

  private final HttpStatus httpStatus;
  private final int code;
  private final String message;
}
