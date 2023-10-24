package org.penekhun.wanted2023.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
@Entity(name = "user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role")
public abstract class UserAccount {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "idx")
  private Long id;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "is_ban")
  private Byte isBan;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Transient
  public String getRole() {
    DiscriminatorValue val = this.getClass().getAnnotation(DiscriminatorValue.class);

    return val == null ? null : val.value();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserAccount user = (UserAccount) o;

    return Objects.equals(id, user.id);
  }

}
