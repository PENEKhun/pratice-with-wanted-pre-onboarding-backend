package org.penekhun.wanted2023.recruitment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;

@Getter
@Entity
@Table(name = "job_posting", schema = "wanted2023")
public class JobPosting {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "idx")
  private Long id;

  @OneToOne
  @JoinColumn(name = "company_id")
  private EnterpriseUserAccount company;

  @Column(name = "recruit_reward")
  private int recruitReward;

  @Column(name = "recruit_position")
  private String recruitPosition;

  @Column(name = "description")
  private String description;

  @Column(name = "required_skill")
  private String requiredSkill;

  @Builder
  public JobPosting(int recruitReward, String recruitPosition, String description,
      String requiredSkill) {
    this.recruitReward = recruitReward;
    this.recruitPosition = recruitPosition;
    this.description = description;
    this.requiredSkill = requiredSkill;
  }

  public JobPosting() {

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    JobPosting that = (JobPosting) o;

    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (company != null ? company.hashCode() : 0);
    result = 31 * result + recruitReward;
    result = 31 * result + (recruitPosition != null ? recruitPosition.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }

  public void setCompany(EnterpriseUserAccount company) {
    this.company = company;
  }
}
