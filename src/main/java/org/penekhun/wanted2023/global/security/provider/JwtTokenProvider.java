package org.penekhun.wanted2023.global.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.penekhun.wanted2023.global.exception.CustomException;
import org.penekhun.wanted2023.global.exception.ExceptionCode;
import org.penekhun.wanted2023.global.security.auth.CustomUserDetailsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {

  private static final String TOKEN = "token";
  private static final String USERNAME = "username";
  private final String secret;
  private final long tokenValidityInMilliseconds;
  private final CustomUserDetailsService userDetailsService;
  private Key key;

  public JwtTokenProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
      CustomUserDetailsService userDetailsService) {
    this.secret = secret;
    this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public void afterPropertiesSet() {
    // 빈 생성시 키 값 전달
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public Map<String, String> createToken(String username) {

    long now = (new Date()).getTime();
    Date validity = new Date(now + this.tokenValidityInMilliseconds);

    Map<String, String> map = new HashMap<>();
    map.put(TOKEN, Jwts.builder()
        .claim(USERNAME, username)
        .signWith(key, SignatureAlgorithm.HS512)
        .setExpiration(validity)
        .compact());
    map.put("tokenExpired", String.valueOf(validity));

    return map;
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
    String username = claims.get(USERNAME, String.class);

    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public boolean validateToken(String accessToken) {
    try {
      parseToken(accessToken);
    } catch (ExpiredJwtException e) {
      throw new CustomException(ExceptionCode.EXPIRED_TOKEN);
    } catch (Exception e) {
      return false;
    }
    
    return true;
  }

  private void parseToken(String accessToken) {
    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
  }

}

