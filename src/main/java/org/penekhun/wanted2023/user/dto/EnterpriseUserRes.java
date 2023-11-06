package org.penekhun.wanted2023.user.dto;

import lombok.Builder;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;

@Builder
public record EnterpriseUserRes(
    long id,
    String name,
    String nationCode,
    String provinceCode
) {

  public static EnterpriseUserRes from(EnterpriseUserAccount company) {
    return EnterpriseUserRes.builder()
        .id(company.getId())
        .name(company.getName())
        .nationCode(company.getNationCode())
        .provinceCode(company.getProvinceCode())
        .build();
  }
}
