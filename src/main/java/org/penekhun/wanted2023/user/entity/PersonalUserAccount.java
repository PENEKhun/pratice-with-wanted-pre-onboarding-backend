package org.penekhun.wanted2023.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("PERSONAL")
public class PersonalUserAccount extends UserAccount {

  @Column(name = "personal_name")
  private String name;

  @Column(name = "personal_brith")
  private LocalDate birth;

}
