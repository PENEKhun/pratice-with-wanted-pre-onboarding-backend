package org.penekhun.wanted2023.global.docs;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@Import(RestDocsConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected RestDocumentationResultHandler restDocs;
  protected ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp(final RestDocumentationContextProvider provider,
      final WebApplicationContext context) {

    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
        .alwaysDo(print())
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
}
