package org.penekhun.wanted2023.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("PERSONAL")
public class PersonalUserAccount extends UserAccount {

  @Column(name = "personal_name")
  private String name;

  @Column(name = "personal_brith")
  private LocalDate birth;

  @Builder
  public PersonalUserAccount(String username, String password, boolean isBan, String name,
      LocalDate birth) {
    super(username, password, isBan);
    this.name = name;
    this.birth = birth;
  }
}
