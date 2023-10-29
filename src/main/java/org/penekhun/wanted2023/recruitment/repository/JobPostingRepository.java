package org.penekhun.wanted2023.recruitment.repository;

import org.penekhun.wanted2023.recruitment.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
  
}
