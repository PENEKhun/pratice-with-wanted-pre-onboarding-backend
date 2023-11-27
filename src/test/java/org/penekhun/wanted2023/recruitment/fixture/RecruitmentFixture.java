package org.penekhun.wanted2023.recruitment.fixture;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import java.time.LocalDateTime;
import net.jqwik.api.Arbitraries;
import net.jqwik.time.api.DateTimes;
import org.penekhun.wanted2023.recruitment.dto.request.JobPostingCreateReq;
import org.penekhun.wanted2023.recruitment.entity.JobPosting;

public class RecruitmentFixture {

  public static JobPosting CreateJobPosting() {
    FixtureMonkey fixture = FixtureMonkey.builder()
        .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
        .build();

    return fixture.giveMeBuilder(JobPosting.class)
        .set("recruitReward",
            Arbitraries.integers().between(0, 100000000))
        .set("recruitPosition",
            Arbitraries.strings().alpha().numeric().ofMinLength(1).ofMaxLength(30))
        .set("description",
            Arbitraries.strings().all().ofMinLength(1).ofMaxLength(10000))
        .set("requiredSkill",
            Arbitraries.strings().alpha().numeric().ofMinLength(4).ofMaxLength(20))
        .set("createdAt",
            DateTimes.dateTimes().atTheLatest(LocalDateTime.now()))
        .set("updatedAt", null)
        .set("deletedAt", null)
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
