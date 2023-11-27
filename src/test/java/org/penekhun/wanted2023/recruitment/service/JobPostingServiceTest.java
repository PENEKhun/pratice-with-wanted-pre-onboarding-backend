package org.penekhun.wanted2023.recruitment.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.penekhun.wanted2023.recruitment.fixture.RecruitmentFixture.CreateJobPosting;
import static org.penekhun.wanted2023.recruitment.fixture.RecruitmentFixture.CreateJobPostingReq;
import static org.penekhun.wanted2023.user.UserFixture.CreateEnterpriseUserAccount;

import jakarta.validation.ValidationException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.penekhun.wanted2023.IntegrationTestSupport;
import org.penekhun.wanted2023.global.exception.CustomException;
import org.penekhun.wanted2023.global.exception.ExceptionCode;
import org.penekhun.wanted2023.recruitment.dto.request.JobPostingCreateReq;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingCreateRes;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingSearchRes;
import org.penekhun.wanted2023.recruitment.entity.JobPosting;
import org.penekhun.wanted2023.recruitment.repository.JobPostingRepository;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;
import org.penekhun.wanted2023.user.repository.EnterpriseAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DisplayName("JobPostingService 테스트")
class JobPostingServiceTest extends IntegrationTestSupport {


  @Autowired
  JobPostingService jobPostingService;

  @Autowired
  JobPostingRepository jobPostingRepository;

  @Autowired
  EnterpriseAccountRepository enterpriseAccountRepository;

  @Nested
  @DisplayName("getJobPostings 메서드는")
  class getJobPostings {

    EnterpriseUserAccount enterpriseAccount = CreateEnterpriseUserAccount();
    ArrayList<JobPosting> jobPostings = new ArrayList<>();

    @BeforeEach
    void setUp() {
      // clear
      jobPostingRepository.deleteAllInBatch();

      // 본격적인 사전 데이터 준비
      enterpriseAccount = CreateEnterpriseUserAccount();
      enterpriseAccountRepository.save(enterpriseAccount);

      for (int i = 0; i < 50; i++) {
        jobPostings.add(CreateJobPosting());
      }
      jobPostings.forEach(jobPosting -> jobPosting.setCompany(enterpriseAccount));
      jobPostingRepository.saveAll(jobPostings);
    }

    @AfterEach
    void tearDown() {
      jobPostingRepository.deleteAll(jobPostings);
      enterpriseAccountRepository.delete(enterpriseAccount);
    }

    @RepeatedTest(5)
    @DisplayName("모든 인자가 정상일때 정상적으로 채용 공고를 조회합니다.")
    void happyCase() {
      // given
      int page = 0;
      int size = jobPostings.size();
      Pageable pageable = PageRequest.of(page, size);

      // when
      Page<JobPostingSearchRes> response = jobPostingService.getJobPostings(pageable);

      // then
      assertThat(response)
          .as("content", "totalElements", "totalPages", "number", "size", "numberOfElements")
          .hasNoNullFieldsOrProperties()
          .extracting(
              "content",
              "numberOfElements"
          ).containsExactlyInAnyOrder(
              jobPostings.stream().map(JobPostingSearchRes::from).toList(),
              jobPostings.size()
          );
    }

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
      JobPostingCreateReq arg = CreateJobPostingReq().sample();

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

    @Test
    @DisplayName("채용 보상금이 많이 클 때 예외를 발생시킵니다.")
    void recruitReward_big_fail() {
      // given
      JobPostingCreateReq arg = CreateJobPostingReq()
          .set("recruitReward", 100000001)
          .validOnly(false)
          .sample();

      // when & then
      assertThrows(
          ValidationException.class,
          () -> jobPostingService.createJobPosting(enterpriseAccount, arg)
      );
    }

    @Test
    @DisplayName("채용 보상금이 음수일때 예외를 발생시킵니다.")
    void recruitReward_negative_fail() {
      // given
      JobPostingCreateReq arg = CreateJobPostingReq()
          .set("recruitReward", -1)
          .validOnly(false)
          .sample();

      // when & then
      assertThrows(
          ValidationException.class,
          () -> jobPostingService.createJobPosting(enterpriseAccount, arg)
      );
    }

    @Test
    @DisplayName("채용 포지션이 길때 예외를 발생시킵니다.")
    void recruitPosition_long_fail() {
      // given
      JobPostingCreateReq arg = CreateJobPostingReq()
          .set("recruitPosition", "a".repeat(31))
          .validOnly(false)
          .sample();

      // when & then
      assertThrows(
          ValidationException.class,
          () -> jobPostingService.createJobPosting(enterpriseAccount, arg)
      );
    }

    @Test
    @DisplayName("채용 설명이 길때 예외를 발생시킵니다.")
    void description_long_fail() {
      // given
      JobPostingCreateReq arg = CreateJobPostingReq()
          .set("description", "a".repeat(10001))
          .validOnly(false)
          .sample();

      // when & then
      assertThrows(
          ValidationException.class,
          () -> jobPostingService.createJobPosting(enterpriseAccount, arg)
      );
    }

    @Test
    @DisplayName("요구 스킬이 길때 예외를 발생시킵니다.")
    void requiredSkill_long_fail() {
      // given
      JobPostingCreateReq arg = CreateJobPostingReq()
          .set("requiredSkill", "a".repeat(21))
          .validOnly(false)
          .sample();

      // when & then
      assertThrows(
          ValidationException.class,
          () -> jobPostingService.createJobPosting(enterpriseAccount, arg)
      );
    }
  }

  @Nested
  @DisplayName("updateMyJobPosting 메서드는")
  class updateMyJobPosting {

    @Test
    @DisplayName("삭제할 채용공고 ID가 null이라면 예외를 발생시킵니다.")
    void jobPostId_null_fail() {
      // given
      Long jobPostId = null;
      JobPostingCreateReq arg = CreateJobPostingReq().sample();
      EnterpriseUserAccount me = CreateEnterpriseUserAccount();
      enterpriseAccountRepository.save(me);

      // when & then
      assertThrowsExactly(
          CustomException.class,
          () -> jobPostingService.updateMyJobPosting(me, jobPostId, arg),
          ExceptionCode.INVALID_REQUEST.getMessage()
      );
    }

    @Test
    @DisplayName("수정할때 JobPosting의 UpdatePartly 메서드가 정상적으로 호출됩니다.")
    void updatePartly_called() {
      // given
      EnterpriseUserAccount me = CreateEnterpriseUserAccount();
      enterpriseAccountRepository.save(me);
      JobPostingCreateRes jobPosting = jobPostingService.createJobPosting(
          me,
          new JobPostingCreateReq(
              100,
              "수정전 포지션",
              "수정전 설명",
              "수정전 요구 스킬"
          )
      );

      JobPostingCreateReq arg = new JobPostingCreateReq(
          200,
          "수정후 포지션",
          "수정후 설명",
          "수정후 요구 스킬"
      );

      // when
      jobPostingService.updateMyJobPosting(me, jobPosting.id(), arg);

      // then
      assertThat(jobPostingRepository.findById(jobPosting.id()))
          .as("수정된 채용공고가 정상적으로 저장되어 있어야 합니다.")
          .isPresent()
          .get()
          .extracting(
              "recruitReward",
              "recruitPosition",
              "description",
              "requiredSkill"
          ).containsExactly(
              arg.recruitReward(),
              arg.recruitPosition(),
              arg.description(),
              arg.requiredSkill()
          );
    }


    @Test
    @DisplayName("본인의 채용공고가 아니라면 예외를 발생시킵니다.")
    void only_for_my_jobPosting() {
      // given
      EnterpriseUserAccount me = CreateEnterpriseUserAccount();
      EnterpriseUserAccount notMe = CreateEnterpriseUserAccount();
      enterpriseAccountRepository.save(me);
      enterpriseAccountRepository.save(notMe);

      JobPostingCreateReq arg = CreateJobPostingReq().sample();
      JobPostingCreateRes jobPosting = jobPostingService.createJobPosting(me, arg);

      // when & then
      assertThrowsExactly(
          CustomException.class,
          () -> jobPostingService.updateMyJobPosting(notMe, jobPosting.id(), arg),
          ExceptionCode.INVALID_REQUEST.getMessage()
      );
    }

  }

  @Nested
  @DisplayName("deleteMyJobPostings 메서드는")
  class deleteMyJobPosting {

    @Test
    @DisplayName("본인의 채용공고를 정상적으로 삭제합니다.")
    void happyCase() {
      // given
      EnterpriseUserAccount me = CreateEnterpriseUserAccount();
      enterpriseAccountRepository.save(me);
      JobPostingCreateRes jobPosting = jobPostingService.createJobPosting(me,
          CreateJobPostingReq().sample());

      // when
      jobPostingService.deleteMyJobPosting(me, jobPosting.id());

      // then
      assertThat(jobPostingRepository.findById(jobPosting.id()))
          .as("삭제 후 조회 결과가 없어야 합니다.")
          .isEmpty();
    }

    @Test
    @DisplayName("본인의 채용공고가 아닐때 예외를 발생시킵니다.")
    void cant_remove_jobPosting_NotMine() {
      // given
      EnterpriseUserAccount me = CreateEnterpriseUserAccount();
      EnterpriseUserAccount notMe = CreateEnterpriseUserAccount();
      enterpriseAccountRepository.save(me);
      enterpriseAccountRepository.save(notMe);
      JobPostingCreateRes jobPosting = jobPostingService.createJobPosting(me,
          CreateJobPostingReq().sample());

      // when & then
      assertThrowsExactly(
          CustomException.class,
          () -> jobPostingService.deleteMyJobPosting(notMe, jobPosting.id()),
          ExceptionCode.INVALID_REQUEST.getMessage()
      );
    }

  }

}