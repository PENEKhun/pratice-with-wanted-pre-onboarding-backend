package org.penekhun.wanted2023.recruitment.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.penekhun.wanted2023.global.rest_controller.ApiResponse;
import org.penekhun.wanted2023.global.security.CurrentUser;
import org.penekhun.wanted2023.global.security.dto.SecurityRole.ROLES;
import org.penekhun.wanted2023.recruitment.dto.request.JobPostingCreateReq;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingCreateRes;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingSearchRes;
import org.penekhun.wanted2023.recruitment.service.JobPostingService;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class JobPostingController {

  private final JobPostingService jobPostingService;

  @GetMapping("/job-posting")
  public ApiResponse<Page<JobPostingSearchRes>> getJobPostings(Pageable pageable) {
    return ApiResponse.ok(jobPostingService.getJobPostings(pageable));
  }

  @Secured(ROLES.ENTERPRISE)
  @PostMapping("/job-posting")
  public ApiResponse<JobPostingCreateRes> createJobPosting(
      @CurrentUser EnterpriseUserAccount enterpriseUser,
      @Valid @RequestBody JobPostingCreateReq jobPostingReq) {
    return ApiResponse.ok(jobPostingService.createJobPosting(enterpriseUser, jobPostingReq));
  }

  @Secured(ROLES.ENTERPRISE)
  @PatchMapping("/job-posting/{jobPostId}")
  public ApiResponse<JobPostingCreateRes> updateMyJobPosting(
      @CurrentUser EnterpriseUserAccount enterpriseUser,
      @NotNull @PathVariable Long jobPostId,
      @Valid @RequestBody JobPostingCreateReq jobPostingReq) {
    return ApiResponse.ok(
        jobPostingService.updateMyJobPosting(enterpriseUser, jobPostId, jobPostingReq));
  }


  @Secured(ROLES.ENTERPRISE)
  @ResponseStatus(NO_CONTENT)
  @DeleteMapping("/job-posting/{jobPostId}")
  public void deleteMyJobPosting(
      @CurrentUser EnterpriseUserAccount enterpriseUser,
      @NotNull @PathVariable Long jobPostId) {
    jobPostingService.deleteMyJobPosting(enterpriseUser, jobPostId);
  }

}
