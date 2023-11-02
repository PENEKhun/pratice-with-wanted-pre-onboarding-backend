package org.penekhun.wanted2023.recruitment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.penekhun.wanted2023.global.docs.RestDocsSupport;
import org.penekhun.wanted2023.recruitment.dto.request.JobPostingCreateReq;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingCreateRes;
import org.penekhun.wanted2023.recruitment.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(controllers = JobPostingController.class)
@DisplayName("채용공고 컨트롤러 테스트")
class JobPostingControllerTest extends RestDocsSupport {

  @MockBean
  JobPostingService jobPostingService;

  @Autowired
  JobPostingController jobPostingController;

  @Test
  @DisplayName("채용 공고 생성에 성공한다.")
  void create_job_posting() throws Exception {

    var input = new JobPostingCreateReq(
        1000000,
        "개발자",
        "개발자를 채용합니다."
    );

    given(jobPostingService.createJobPosting(any(), any()))
        .willReturn(JobPostingCreateRes.builder()
            .id(1L)
            .recruitPosition(input.recruitPosition())
            .recruitReward(input.recruitReward())
            .description(input.description())
            .build());

    mockMvc
        .perform(
            post("/api/v1/job-posting")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            requestFields(
                fieldWithPath("recruitPosition")
                    .type(JsonFieldType.STRING)
                    .description("채용 포지션"),
                fieldWithPath("recruitReward")
                    .optional()
                    .type(JsonFieldType.NUMBER)
                    .description("채용 보상"),
                fieldWithPath("description")
                    .type(JsonFieldType.STRING)
                    .description("채용 공고 설명")
            ),
            responseFields(responseCommon())
                .andWithPrefix("data.",
                    fieldWithPath("id")
                        .type(JsonFieldType.NUMBER)
                        .description("채용 공고 ID"),
                    fieldWithPath("recruitPosition")
                        .type(JsonFieldType.STRING)
                        .description("채용 포지션"),
                    fieldWithPath("recruitReward")
                        .type(JsonFieldType.NUMBER)
                        .description("채용 보상"),
                    fieldWithPath("description")
                        .type(JsonFieldType.STRING)
                        .description("채용 공고 설명")
                )
        ))
        .andReturn();
  }

      @TestWithEnterpriseAccount
      @DisplayName("채용 보상금이 음수라면, 400 응답을 받습니다.")
      void reward_valid_err() throws Exception {
        // given
        String given = "-1";
        Map<String, String> input = new HashMap<>();
        input.put("recruitPosition", "개발자");
        input.put("recruitReward", given);
        input.put("description", "개발자를 모집합니다.");

        // when & then
        mockMvc
            .perform(
                post("/api/v1/job-posting")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isBadRequest());
      }

      @TestWithEnterpriseAccount
      @DisplayName("채용 포지션이 null 이라면, 400 응답을 받습니다.")
      void position_null_valid_err() throws Exception {
        // given
        String given = null;
        Map<String, String> input = new HashMap<>();
        input.put("recruitPosition", given);
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
            .andExpectAll(
                status().isBadRequest(),
                jsonPath("$.data").value("채용 포지션을 입력해주세요."));
      }

      @TestWithEnterpriseAccount
      @DisplayName("채용 포지션이 길다면, 400 응답을 받습니다.")
      void position_len_valid_err() throws Exception {
        // given
        String given = "a".repeat(31);
        Map<String, String> input = new HashMap<>();
        input.put("recruitPosition", given);
        input.put("recruitReward", "10000");
        input.put("description", "개발자를 모집합니다.");

        // when & then
        mockMvc
            .perform(
                post("/api/v1/job-posting")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
            .andExpectAll(
                status().isBadRequest(),
                jsonPath("$.data").value("채용 포지션은 30자 이하로 입력해주세요."));
      }
    }
  }

  @Nested
  @Disabled
  @DisplayName("채용 공고 삭제시에")
  class delete_job_posting {

    @Nested
    @DisplayName("개인 계정은")
    class with_personal_account {

      @TestWithPersonalAccount
      @DisplayName("401 응답을 받습니다.")
      void personal_cant_delete_jobPosting() throws Exception {
        mockMvc
            .perform(
                post("/api/v1/job-posting/1")
                    .with(csrf()))
            .andExpect(status().isBadRequest());
      }

    }

    @Nested
    @DisplayName("기업 계정은")
    class with_enterprise_account {

    }
  }
}