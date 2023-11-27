package org.penekhun.wanted2023.recruitment.service;

import jakarta.validation.Valid;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.penekhun.wanted2023.global.exception.CustomException;
import org.penekhun.wanted2023.global.exception.ExceptionCode;
import org.penekhun.wanted2023.recruitment.dto.request.JobPostingCreateReq;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingCreateRes;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingSearchRes;
import org.penekhun.wanted2023.recruitment.entity.JobPosting;
import org.penekhun.wanted2023.recruitment.repository.JobPostingRepository;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobPostingService {

  private final JobPostingRepository jobPostingRepository;

  private JobPostingCreateRes createResponse(JobPosting jobPosting) {
    return JobPostingCreateRes.builder()
        .id(jobPosting.getId())
        .recruitReward(jobPosting.getRecruitReward())
        .recruitPosition(jobPosting.getRecruitPosition())
        .description(jobPosting.getDescription())
        .requiredSkill(jobPosting.getRequiredSkill())
        .build();
  }

  private Predicate<JobPosting> myJobPost(
      EnterpriseUserAccount enterpriseUser) {
    return jobPosting -> jobPosting.getCompany().equals(enterpriseUser);
  }

  @Transactional
  public JobPostingCreateRes createJobPosting(EnterpriseUserAccount enterpriseUser,
      @Valid JobPostingCreateReq jobPostingReq) {
    if (enterpriseUser == null) {
      throw new CustomException(ExceptionCode.INVALID_REQUEST);
    }

    JobPosting jobPosting = JobPosting.builder()
        .description(jobPostingReq.description())
        .recruitPosition(jobPostingReq.recruitPosition())
        .recruitReward(jobPostingReq.recruitReward())
        .requiredSkill(jobPostingReq.requiredSkill())
        .build();
    jobPosting.setCompany(enterpriseUser);
    jobPostingRepository.save(jobPosting);

    return createResponse(jobPosting);
  }

  @Transactional
  public void deleteMyJobPosting(EnterpriseUserAccount enterpriseUser, Long jobPostId) {
    if (enterpriseUser == null || jobPostId == null) {
      throw new CustomException(ExceptionCode.INVALID_REQUEST);
    }

    jobPostingRepository.findById(jobPostId)
        .filter(myJobPost(enterpriseUser))
        .ifPresentOrElse(
            jobPostingRepository::delete,
            () -> {
              throw new CustomException(ExceptionCode.INVALID_REQUEST);
            });
  }

  public Page<JobPostingSearchRes> getJobPostings(Pageable pageable) {
    Page<JobPosting> result = jobPostingRepository.findAll(pageable);
    return new PageImpl<>(
        result.getContent().stream()
            .map(JobPostingSearchRes::from)
            .toList(),
        pageable,
        result.getTotalElements()
    );
  }

  @Transactional
  public JobPostingCreateRes updateMyJobPosting(EnterpriseUserAccount enterpriseUser,
      Long jobPostId, @Valid JobPostingCreateReq jobPostingReq) {
    if (jobPostId == null || enterpriseUser == null) {
      throw new CustomException(ExceptionCode.INVALID_REQUEST);
    }

    JobPosting jobPosting = jobPostingRepository.findById(jobPostId)
        .filter(myJobPost(enterpriseUser))
        .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_REQUEST));

    jobPosting.updatePartly(jobPostingReq);
    return createResponse(jobPosting);
  }
}
