package org.penekhun.wanted2023.global.security.fixture;

import org.penekhun.wanted2023.global.security.auth.CustomUser;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class EnterpriseSecurityContextFactory implements
    WithSecurityContextFactory<TestWithEnterpriseAccount> {

  @Override
  public SecurityContext createSecurityContext(TestWithEnterpriseAccount annotation) {
    final SecurityContext context = SecurityContextHolder.createEmptyContext();

    EnterpriseUserAccount account = EnterpriseUserAccount.builder().username(annotation.username())
        .password(annotation.password()).build();

    CustomUser userDetails = new CustomUser(account, false);
    final Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());
    context.setAuthentication(auth);
    return context;
  }
}
