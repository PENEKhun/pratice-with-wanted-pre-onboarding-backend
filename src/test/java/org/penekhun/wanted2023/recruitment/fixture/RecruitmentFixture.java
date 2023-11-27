package org.penekhun.wanted2023.recruitment.fixture;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import net.jqwik.api.Arbitraries;
import org.penekhun.wanted2023.recruitment.dto.request.JobPostingCreateReq;
import org.penekhun.wanted2023.recruitment.entity.JobPosting;

public class RecruitmentFixture {

  public static JobPosting CreateJobPosting() {
    FixtureMonkey fixture = FixtureMonkey.builder()
        .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
        .build();

    return fixture.giveMeBuilder(JobPosting.class)
        .set("recruitReward",
            Arbitraries.integers().between(0, 100000000).sample())
        .set("recruitPosition",
            Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(30))
        .set("description",
            Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(10000))
        .set("requiredSkill",
            Arbitraries.strings().withCharRange('a', 'z').ofMinLength(4).ofMaxLength(20))
        .build().sample();
  }

  public static ArbitraryBuilder<JobPostingCreateReq> CreateJobPostingReq() {

    FixtureMonkey fixture = FixtureMonkey.builder()
        .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
        .plugin(new JakartaValidationPlugin())
        .build();

    return fixture.giveMeBuilder(JobPostingCreateReq.class);
  }

}
