package org.penekhun.wanted2023.global.security.fixture;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

@Test
@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "홍길동", roles = "PERSONAL")
public @interface TestWithPersonalAccount {

}
