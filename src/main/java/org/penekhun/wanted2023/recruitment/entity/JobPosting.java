package org.penekhun.wanted2023.recruitment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.penekhun.wanted2023.recruitment.dto.request.JobPostingCreateReq;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "job_posting", schema = "wanted2023")
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE idx = ?")
@Where(clause = "deleted_at IS NULL")
@EntityListeners(AuditingEntityListener.class)
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

  @CreatedDate
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Builder
  public JobPosting(int recruitReward, String recruitPosition, String description,
      String requiredSkill) {
    this.recruitReward = recruitReward;
    this.recruitPosition = recruitPosition;
    this.description = description;
    this.requiredSkill = requiredSkill;
  }

  public static JobPosting of(JobPostingCreateReq jobPostingReq) {
    return JobPosting.builder()
        .description(jobPostingReq.description())
        .recruitPosition(jobPostingReq.recruitPosition())
        .recruitReward(jobPostingReq.recruitReward())
        .requiredSkill(jobPostingReq.requiredSkill())
        .build();
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
    return Objects.hashCode(id);
  }

  public void setCompany(EnterpriseUserAccount company) {
    this.company = company;
  }

  public void updatePartly(@NotNull JobPostingCreateReq jobPostingReq) {
    if (StringUtils.hasText(jobPostingReq.description())) {
      this.recruitReward = jobPostingReq.recruitReward();
    }
    if (StringUtils.hasText(jobPostingReq.recruitPosition())) {
      this.recruitPosition = jobPostingReq.recruitPosition();
    }
    if (StringUtils.hasText(jobPostingReq.description())) {
      this.description = jobPostingReq.description();
    }
    if (StringUtils.hasText(jobPostingReq.requiredSkill())) {
      this.requiredSkill = jobPostingReq.requiredSkill();
    }
  }
}
