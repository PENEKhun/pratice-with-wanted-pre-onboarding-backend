package org.penekhun.wanted2023.recruitment.dto.response;

import lombok.Builder;

@Builder
public record JobPostingCreateRes(
    Long id,
    int recruitReward,
    String recruitPosition,
    String description
) {

}
