package org.penekhun.wanted2023.global.security.fixture;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithSecurityContext;

@Test
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = PersonalSecurityContextFactory.class)
public @interface TestWithPersonalAccount {

  String username() default "홍길동";

  String password() default "password";

}
