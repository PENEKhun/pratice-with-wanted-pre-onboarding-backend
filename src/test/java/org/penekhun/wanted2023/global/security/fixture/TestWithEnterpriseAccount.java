package org.penekhun.wanted2023.global.security.fixture;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithSecurityContext;

@Test
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = EnterpriseSecurityContextFactory.class)
public @interface TestWithEnterpriseAccount {

  String username() default "samsung";

  String password() default "password";

}