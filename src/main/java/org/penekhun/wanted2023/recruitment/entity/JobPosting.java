package org.penekhun.wanted2023.recruitment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "job_posting", schema = "wanted2023")
public class JobPosting {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "idx")
  private long id;

  @Column(name = "company_id")
  private Long companyId;
  private Long id;

  @Column(name = "recruit_reward")
  private int recruitReward;

  @Column(name = "recruit_position")
  private String recruitPosition;

  @Column(name = "description")
  private String description;

  @Builder
  public JobPosting(Long companyId, int recruitReward, String recruitPosition, String description) {
    this.companyId = companyId;
    this.recruitReward = recruitReward;
    this.recruitPosition = recruitPosition;
    this.description = description;
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

    return id == that.id;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
    result = 31 * result + recruitReward;
    result = 31 * result + (recruitPosition != null ? recruitPosition.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }
}
