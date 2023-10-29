package org.penekhun.wanted2023.recruitment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.penekhun.wanted2023.recruitment.dto.request.JobPostingCreateReq;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingCreateRes;
import org.penekhun.wanted2023.recruitment.service.JobPostingService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class JobPostingController {

  private final JobPostingService jobPostingService;

  @Secured("ROLE_ENTERPRISE")
  @PostMapping("/job-posting")
  public JobPostingCreateRes createJobPosting(
      @Valid @RequestBody JobPostingCreateReq jobPostingReq) {
    return jobPostingService.createJobPosting(jobPostingReq);
  }

}
