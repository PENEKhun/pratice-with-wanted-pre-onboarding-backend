package org.penekhun.wanted2023.global.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@Import(RestDocsConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

  protected ObjectMapper objectMapper = new ObjectMapper();
  protected RestDocumentationResultHandler restDocs = new RestDocsConfiguration().restDocumentationResultHandler();
  protected MockMvc mockMvc;

  @BeforeEach
  void setUp(final RestDocumentationContextProvider provider) {
    this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
        .apply(documentationConfiguration(provider))
        .alwaysDo(restDocs)
        .build();
  }

  protected FieldDescriptor[] responseCommon() {
    return new FieldDescriptor[]{
        fieldWithPath("code")
            .type(JsonFieldType.NUMBER)
            .description("응답 코드"),
        fieldWithPath("status")
            .type(JsonFieldType.STRING)
            .description("응답 상태"),
        fieldWithPath("message")
            .type(JsonFieldType.STRING)
            .description("응답 메시지"),
        fieldWithPath("data")
            .type(JsonFieldType.OBJECT)
            .description("응답 데이터")
    };
  }

  protected abstract Object initController();
}
