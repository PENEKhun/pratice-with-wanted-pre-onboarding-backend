package org.penekhun.wanted2023.recruitment.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("JobPosting 엔티티 테스트")
class JobPostingTest {

  @Nested
  @DisplayName("equals and hashCode 메소드의")
  class equalsAndHashCode {

    @Test
    @DisplayName("jobPosting 객체의 id가 같으면 true를 반환한다.")
    void trueCase() {
      // given
      JobPosting one = new JobPosting();
      JobPosting two = new JobPosting();
      ReflectionTestUtils.setField(one, "id", 1L);
      ReflectionTestUtils.setField(two, "id", 1L);

      // when & then
      assertTrue(one.equals(two) && two.equals(one));
      assertEquals(one.hashCode(), two.hashCode());
    }

    @Test
    @DisplayName("jobPosting 객체의 id가 다르면 false를 반환한다")
    void falseCase() {
      // given
      JobPosting jobPosting1 = new JobPosting();
      JobPosting jobPosting2 = new JobPosting();
      ReflectionTestUtils.setField(jobPosting1, "id", 1L);
      ReflectionTestUtils.setField(jobPosting2, "id", 2L);

      // when & then
      assertFalse(jobPosting1.equals(jobPosting2) && jobPosting2.equals(jobPosting1));
      assertNotEquals(jobPosting1.hashCode(), jobPosting2.hashCode());
    }
  }

}