package org.penekhun.wanted2023.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("enterprise")
public class EnterpriseUserAccount extends UserAccount {

  @Column(name = "enterprise_name")
  private String name;

  @Column(name = "enterprise_nation_code")
  private String nationCode;

  @Column(name = "enterprise_region_code")
  private String regionCode;

}
