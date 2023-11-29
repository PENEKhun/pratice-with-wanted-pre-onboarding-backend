package org.penekhun.wanted2023.global.security.provider;

import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.penekhun.wanted2023.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;

class JwtTokenProviderTest extends IntegrationTestSupport {

  @Autowired
  JwtTokenProvider jwtTokenProvider;

  @Test
  @DisplayName("토큰 생성 시 Map에 토큰과 만료 시간이 포함되어야 한다.")
  void createToken() {
    // given
    var username = "username";
    var now = new Date();

    // when
    var tokenMap = jwtTokenProvider.createToken(username, now);

    // then
    assert tokenMap.containsKey("token");
    assert tokenMap.containsKey("tokenExpired");
  }

  @Test
  void getAuthentication() {
  }

  @Test
  void validateToken() {
  }
}