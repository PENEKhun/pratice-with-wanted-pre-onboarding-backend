package org.penekhun.wanted2023.user;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import net.jqwik.api.Arbitraries;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;

public class UserFixture {

  public static EnterpriseUserAccount CreateEnterpriseUserAccount() {
    FixtureMonkey fixture = FixtureMonkey.builder()
        .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
        .build();

    return fixture.giveMeBuilder(EnterpriseUserAccount.class)
        .set("username",
            Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(10))
        .set("password",
            "${Bcrypt}%s".formatted(
                Arbitraries.strings().withCharRange('a', 'z').ofMinLength(20).ofMaxLength(30)
                    .sample()))
        .set("isBan",
            Arbitraries.integers().between(0, 1).map(i -> i == 1))
        .set("name",
            Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(10))
        .set("nationCode",
            Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(5))
        .set("provinceCode",
            Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(5))
        .build().sample();
  }

}
