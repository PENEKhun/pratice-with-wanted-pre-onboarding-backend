package org.penekhun.wanted2023.recruitment.dto.response;

import lombok.Builder;
import org.penekhun.wanted2023.recruitment.entity.JobPosting;

@Builder
public record JobPostingSearchRes(
    Long id,
    int recruitReward,
    String recruitPosition,
    String description,
    String requiredSkill
) {

  public static JobPostingSearchRes from(JobPosting jobPosting) {
    return JobPostingSearchRes.builder()
        .id(jobPosting.getId())
        .recruitReward(jobPosting.getRecruitReward())
        .recruitPosition(jobPosting.getRecruitPosition())
        .description(jobPosting.getDescription())
        .requiredSkill(jobPosting.getRequiredSkill())
        .build();
  }
}
