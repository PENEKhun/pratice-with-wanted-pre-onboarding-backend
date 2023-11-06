package org.penekhun.wanted2023.recruitment.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.penekhun.wanted2023.recruitment.fixture.RecruitmentFixture.CreateJobPosting;
import static org.penekhun.wanted2023.recruitment.fixture.RecruitmentFixture.CreateJobPostingReq;
import static org.penekhun.wanted2023.user.UserFixture.CreateEnterpriseUserAccount;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.penekhun.wanted2023.recruitment.dto.request.JobPostingCreateReq;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingCreateRes;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingSearchRes;
import org.penekhun.wanted2023.recruitment.entity.JobPosting;
import org.penekhun.wanted2023.recruitment.repository.JobPostingRepository;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;
import org.penekhun.wanted2023.user.repository.EnterpriseAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@DisplayName("JobPostingService 테스트")
class JobPostingServiceTest {


  @Autowired
  JobPostingService jobPostingService;

  @Autowired
  JobPostingRepository jobPostingRepository;

  @Autowired
  EnterpriseAccountRepository enterpriseAccountRepository;

  @BeforeEach
  void setUp() {
    enterpriseAccountRepository.save(enterpriseAccount);
  }

  @Nested
  @DisplayName("createJobPosting 메서드는")
  class createJobPosting {

    EnterpriseUserAccount enterpriseAccount = CreateEnterpriseUserAccount();

    @BeforeEach
    void setUp() {
      enterpriseAccountRepository.save(enterpriseAccount);
    }

    @AfterEach
    void tearDown() {
      enterpriseAccountRepository.delete(enterpriseAccount);
    }

    @RepeatedTest(100)
    @DisplayName("모든 인자가 정상일때 정상적으로 채용 공고를 생성합니다.")
    void happyCase() {
      // given
      JobPostingCreateReq arg = CreateJobPostingReq();

      // when
      JobPostingCreateRes response = jobPostingService.createJobPosting(enterpriseAccount, arg);

      // then
      assertThat(response)
          .as("id", "recruitReward", "recruitPosition", "description")
          .hasNoNullFieldsOrProperties()
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