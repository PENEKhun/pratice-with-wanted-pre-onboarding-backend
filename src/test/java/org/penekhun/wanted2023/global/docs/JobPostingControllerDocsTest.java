package org.penekhun.wanted2023.global.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.penekhun.wanted2023.global.security.fixture.TestWithEnterpriseAccount;
import org.penekhun.wanted2023.recruitment.controller.JobPostingController;
import org.penekhun.wanted2023.recruitment.dto.request.JobPostingCreateReq;
import org.penekhun.wanted2023.recruitment.dto.response.JobPostingCreateRes;
import org.penekhun.wanted2023.recruitment.service.JobPostingService;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class JobPostingControllerDocsTest extends RestDocsSupport {

  private final JobPostingService jobPostingService = mock(JobPostingService.class);

  @Override
  protected Object initController() {
    return new JobPostingController(jobPostingService);
  }

  @TestWithEnterpriseAccount
  @DisplayName("채용공고 등록 API")
  void createJobPosting() throws Exception {
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
            responseFields(this.responseCommon())
                .and(
                    fieldWithPath("data")
                        .type(JsonFieldType.OBJECT),
                    fieldWithPath("data.id")
                        .type(JsonFieldType.NUMBER)
                        .description("채용 공고 ID"),
                    fieldWithPath("data.recruitPosition")
                        .type(JsonFieldType.STRING)
                        .description("채용 포지션"),
                    fieldWithPath("data.recruitReward")
                        .type(JsonFieldType.NUMBER)
                        .description("채용 보상"),
                    fieldWithPath("data.description")
                        .type(JsonFieldType.STRING)
                        .description("채용 공고 설명")
                )
        ))
        .andReturn();
  }
}
