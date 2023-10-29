package org.penekhun.wanted2023.recruitment.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.penekhun.wanted2023.global.security.fixture.TestWithEnterpriseAccount;
import org.penekhun.wanted2023.global.security.fixture.TestWithPersonalAccount;
import org.penekhun.wanted2023.recruitment.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = JobPostingController.class)
@DisplayName("채용공고 컨트롤러 테스트")
class JobPostingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private JobPostingService jobPostingService;

  @Nested
  @DisplayName("채용 공고 생성시에")
  class create_job_posting {

    @TestWithPersonalAccount
    @DisplayName("개인 계정은 채용 공고 생성 권한이 없습니다.")
    void personal_cant_create_jobPosting() throws Exception {
      mockMvc
          .perform(
              post("/api/v1/job-posting")
                  .with(csrf()))
          .andExpect(status().isBadRequest());
    }

    @Nested
    @DisplayName("기업 계정은")
    class with_enterprise_account {

      @TestWithEnterpriseAccount
      @DisplayName("정상 입력값으로 채용 공고 생성시 200 OK 응답을 받습니다.")
      void happy() throws Exception {
        // given
        Map<String, String> input = new HashMap<>();
        input.put("recruitPosition", "개발자");
        input.put("recruitReward", "10000");
        input.put("description", "개발자를 모집합니다.");

        // when & then
        mockMvc
            .perform(
                post("/api/v1/job-posting")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
            .andDo(print())
            .andExpect(status().isOk());
      }

      @TestWithEnterpriseAccount
      @DisplayName("채용 보상금이 음수라면, 400 응답을 받습니다.")
      void reward_valid_err() throws Exception {
        // given
        Map<String, String> input = new HashMap<>();
        input.put("recruitPosition", "개발자");
        input.put("recruitReward", "-1");
        input.put("description", "개발자를 모집합니다.");

        // when & then
        mockMvc
            .perform(
                post("/api/v1/job-posting")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
            .andDo(print())
            .andExpect(status().isBadRequest());
      }

      @TestWithEnterpriseAccount
      @DisplayName("채용 포지션이 Null 이라면, 400 응답을 받습니다.")
      void position_null_valid_err() throws Exception {
        // given
        Map<String, String> input = new HashMap<>();
        input.put("recruitPosition", null);
        input.put("recruitReward", "10000");
        input.put("description", "개발자를 모집합니다.");

        // when & then
        mockMvc
            .perform(
                post("/api/v1/job-posting")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
            .andDo(print())
            .andExpect(status().isBadRequest());
      }

      @TestWithEnterpriseAccount
      @DisplayName("채용 포지션이 '' 이라면, 400 응답을 받습니다.")
      void position_valid_err() throws Exception {
        // given
        Map<String, String> input = new HashMap<>();
        input.put("recruitPosition", "");
        input.put("recruitReward", "10000");
        input.put("description", "개발자를 모집합니다.");

        // when & then
        mockMvc
            .perform(
                post("/api/v1/job-posting")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
            .andDo(print())
            .andExpect(status().isBadRequest());
      }
    }
  }
}