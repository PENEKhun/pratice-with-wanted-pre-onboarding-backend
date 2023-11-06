package org.penekhun.wanted2023.global.docs;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;
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
import org.springframework.restdocs.request.ParameterDescriptor;
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

  protected ParameterDescriptor[] requestPageable() {
    return new ParameterDescriptor[]{
        parameterWithName("page")
            .description("페이지 번호")
            .attributes(key("defaultValue").value(0))
            .optional(),
        parameterWithName("size")
            .description("한 페이지당 항목 수")
            .attributes(key("defaultValue").value(10))
            .optional(),
        parameterWithName("sort")
            .description("정렬 기준 (예: 필드명,desc 또는 id,asc)")
            .attributes(key("defaultValue").value("id,desc"))
            .optional()
    };
  }

  protected FieldDescriptor[] responsePage() {
    return new FieldDescriptor[]{
        fieldWithPath("data.pageable")
            .type(JsonFieldType.STRING)
            .description("페이징 정보 (예: INSTANCE)"),
        fieldWithPath("data.totalElements")
            .type(JsonFieldType.NUMBER)
            .description("데이터 집합의 총 요소 수"),
        fieldWithPath("data.totalPages")
            .type(JsonFieldType.NUMBER)
            .description("총 페이지 수"),
        fieldWithPath("data.last")
            .type(JsonFieldType.BOOLEAN)
            .description("마지막 페이지 여부"),
        fieldWithPath("data.size")
            .type(JsonFieldType.NUMBER)
            .description("한 페이지당 아이템 수"),
        fieldWithPath("data.number")
            .type(JsonFieldType.NUMBER)
            .description("현재 페이지 번호"),
        fieldWithPath("data.sort.empty")
            .type(JsonFieldType.BOOLEAN)
            .description("정렬이 비어있는지 여부"),
        fieldWithPath("data.sort.sorted")
            .type(JsonFieldType.BOOLEAN)
            .description("결과가 정렬되었는지 여부"),
        fieldWithPath("data.sort.unsorted")
            .type(JsonFieldType.BOOLEAN)
            .description("결과가 정렬되지 않았는지 여부"),
        fieldWithPath("data.numberOfElements")
            .type(JsonFieldType.NUMBER)
            .description("현재 페이지의 요소 수"),
        fieldWithPath("data.first")
            .type(JsonFieldType.BOOLEAN)
            .description("첫 번째 페이지 여부"),
        fieldWithPath("data.empty")
            .type(JsonFieldType.BOOLEAN)
            .description("페이지가 비어있는지 여부")
    };
  }
}
