package org.penekhun.wanted2023.recruitment.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.penekhun.wanted2023.global.security.CurrentUser;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;

public record JobPostingCreateReq(
    @Min(value = 0, message = "채용 보상금은 0원 이상이어야 합니다.")
    @Max(value = 100000000, message = "채용 보상금은 1억원 이하로 입력해주세요.")
    int recruitReward,
    @NotBlank(message = "채용 포지션을 입력해주세요")
    String recruitPosition,
    @NotNull(message = "채용 설명을 입력해주세요")
    @Size(min = 1, max = 10000, message = "채용 설명은 1자 이상 10000자 이하로 입력해주세요.")
    String description
) {

}
