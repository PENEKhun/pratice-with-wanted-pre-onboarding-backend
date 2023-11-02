package org.penekhun.wanted2023.global.security.filter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;

@SpringBootTest
@DisplayName("사용자 정의 필터 활성화 확인 테스트")
class FilterEnableTests {

  @Autowired
  private FilterChainProxy filterChainProxy;

  @Test
  @DisplayName("JwtTokenGenFilter가 필터 체인에 등록되어 있어야 합니다.")
  void check_JwtTokenGenFilter_is_enabled() {
    Object givenClass = JwtTokenGenFilter.class;
    boolean exist = filterChainProxy.getFilters("/").stream()
        .anyMatch(filter -> filter.getClass().equals(givenClass));

    assertThat(exist).isTrue();
  }

  @Test
  @DisplayName("JwtTokenChkFilter가 필터 체인에 등록되어 있어야 합니다.")
  void check_JwtTokenChkFilter_is_enabled() {
    Object givenClass = JwtTokenChkFilter.class;
    boolean exist = filterChainProxy.getFilters("/").stream()
        .anyMatch(filter -> filter.getClass().equals(givenClass));

    assertThat(exist).isTrue();
  }

}
