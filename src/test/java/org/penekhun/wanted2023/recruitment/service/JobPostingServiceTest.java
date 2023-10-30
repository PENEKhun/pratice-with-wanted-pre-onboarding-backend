package org.penekhun.wanted2023.recruitment.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.penekhun.wanted2023.recruitment.dto.request.JobPostingCreateReq;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingCreateRes;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;
import org.penekhun.wanted2023.user.repository.EnterpriseAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@DisplayName("JobPostingService 테스트")
class JobPostingServiceTest {

  final EnterpriseUserAccount enterpriseAccount = EnterpriseUserAccount.builder()
      .username("samsung")
      .password("1234")
      .build();

  @Autowired
  JobPostingService jobPostingService;

  @Autowired
  EnterpriseAccountRepository enterpriseAccountRepository;

  @BeforeEach
  void setUp() {
    enterpriseAccountRepository.save(enterpriseAccount);
  }

  @Nested
  @DisplayName("createJobPosting 메서드는")
  class createJobPosting {

    @RepeatedTest(100)
    @DisplayName("모든 인자가 정상일때 정상적으로 채용 공고를 생성합니다.")
    void happyCase() {
      // given
      FixtureMonkey fixture = FixtureMonkey.builder()
          .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
          .plugin(new JakartaValidationPlugin())
          .build();

      JobPostingCreateReq arg = fixture.giveMeOne(JobPostingCreateReq.class);

      // when
      JobPostingCreateRes response = jobPostingService.createJobPosting(enterpriseAccount, arg);

      // then
      assertThat(response)
          .as("id", "recruitReward", "recruitPosition", "description").hasNoNullFieldsOrProperties()
          .extracting(
              "recruitReward",
              "recruitPosition",
              "description"
          ).containsExactly(
              arg.recruitReward(),
              arg.recruitPosition(),
              arg.description()
          );
    }
  }

}