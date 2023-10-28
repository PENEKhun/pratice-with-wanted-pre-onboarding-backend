package org.penekhun.wanted2023.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.penekhun.wanted2023.global.security.provider.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

/**
 * JWT 토큰으로 사용자 인증을 처리하는 필터
 */
@Slf4j
public class JwtTokenChkFilter extends BasicAuthenticationFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String REFRESH_TOKEN_HEADER = "REFRESH";
  private final JwtTokenProvider tokenProvider;

  public JwtTokenChkFilter(
      JwtTokenProvider tokenProvider,
      AuthenticationManager authenticationManager) {
    super(authenticationManager);
    this.tokenProvider = tokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String requestURI = request.getRequestURI();
    String accessToken = resolveToken(request);

    if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
      Authentication authentication = tokenProvider.getAuthentication(accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      log.debug("{} passed TokenChkFilter at {}", authentication.getName(),
          requestURI);
    } else {
      log.debug("token invalid at {}", requestURI);
    }

    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    /* 헤더에서 토큰을 가져옴 */
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
      return bearerToken.substring(TOKEN_PREFIX.length());
    }
    return null;
  }
}

