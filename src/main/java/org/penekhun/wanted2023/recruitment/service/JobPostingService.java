package org.penekhun.wanted2023.recruitment.service;

import lombok.RequiredArgsConstructor;
import org.penekhun.wanted2023.recruitment.dto.request.JobPostingCreateReq;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingCreateRes;
import org.penekhun.wanted2023.recruitment.entity.JobPosting;
import org.penekhun.wanted2023.recruitment.repository.JobPostingRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobPostingService {

  private final JobPostingRepository jobPostingRepository;

  @Transactional
  public JobPostingCreateRes createJobPosting(JobPostingCreateReq jobPostingReq) {
    if (jobPostingReq.enterpriseUser() == null) {
      throw new BadCredentialsException("Invalid enterprise user");
    }

    JobPosting jobPosting = JobPosting.builder()
        .companyId(jobPostingReq.enterpriseUser().getId())
        .description(jobPostingReq.description())
        .recruitPosition(jobPostingReq.recruitPosition())
        .recruitReward(jobPostingReq.recruitReward())
        .build();

    jobPostingRepository.save(jobPosting);

    return new JobPostingCreateRes(
        jobPosting.getId(),
        jobPosting.getCompanyId(),
        jobPosting.getRecruitReward(),
        jobPosting.getRecruitPosition(),
        jobPosting.getDescription()
    );
  }
}
