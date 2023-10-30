package org.penekhun.wanted2023.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("ENTERPRISE")
public class EnterpriseUserAccount extends UserAccount {

  @Column(name = "enterprise_name")
  private String name;

  @Column(name = "enterprise_nation_code")
  private String nationCode;

  @Column(name = "enterprise_province_code")
  private String provinceCode;

  @Builder
  public EnterpriseUserAccount(String username, String password, boolean isBan, String name,
      String nationCode, String provinceCode) {
    super(username, password, isBan);
    this.name = name;
    this.nationCode = nationCode;
    this.provinceCode = provinceCode;
  }

}
