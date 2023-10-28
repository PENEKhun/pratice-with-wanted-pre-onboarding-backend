package org.penekhun.wanted2023.global.security.filter;


import org.penekhun.wanted2023.global.security.provider.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtFilterApplier extends
    AbstractHttpConfigurer<JwtFilterApplier, HttpSecurity> {

  private final JwtTokenProvider tokenProvider;

  public JwtFilterApplier(JwtTokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Override
  public void configure(HttpSecurity http) {
    AuthenticationManager authenticationManager = http.getSharedObject(
        AuthenticationManager.class);
    JwtTokenGenFilter tokenGenFilter = new JwtTokenGenFilter(tokenProvider);
    tokenGenFilter.setAuthenticationManager(authenticationManager);

    JwtTokenChkFilter tokenChkFilter = new JwtTokenChkFilter(tokenProvider, authenticationManager);

    http
        .addFilter(tokenGenFilter)
        .addFilterBefore(tokenChkFilter,
            UsernamePasswordAuthenticationFilter.class);
  }

}