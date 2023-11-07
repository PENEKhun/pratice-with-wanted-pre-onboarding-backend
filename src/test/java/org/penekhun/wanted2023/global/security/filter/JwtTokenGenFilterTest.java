package org.penekhun.wanted2023.global.security.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.penekhun.wanted2023.global.security.filter.JwtTokenChkFilter.AUTHORIZATION_HEADER;
import static org.penekhun.wanted2023.global.security.filter.JwtTokenChkFilter.TOKEN_PREFIX;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.penekhun.wanted2023.global.security.auth.CustomUser;
import org.penekhun.wanted2023.global.security.auth.CustomUserDetailsService;
import org.penekhun.wanted2023.user.entity.PersonalUserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class JwtTokenGenFilterTest {

  @Autowired
  protected MockMvc mockMvc;

  @MockBean
  CustomUserDetailsService customUserDetailsService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp(final RestDocumentationContextProvider provider,
      final WebApplicationContext context) {

    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
        .alwaysDo(print())
        .build();
  }

  @Test
  @DisplayName("로그인 API 동작 여부 테스트")
  void working_login_filter() throws Exception {
    // mocking
    when(customUserDetailsService.loadUserByUsername(any()))
        .thenReturn(
            new CustomUser(
                new PersonalUserAccount(
                    "username",
                    passwordEncoder.encode("password"),
                    false,
                    "name",
                    LocalDate.now()
                ), true));

    mockMvc.perform(MockMvcRequestBuilders.post(JwtTokenGenFilter.LOGIN_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "username": "username",
                  "password": "password"
                }
                """))
        .andDo(
            document("auth/login",
                preprocessRequest(
                    modifyHeaders().remove("Content-Length"),
                    modifyHeaders().remove("Host"),
                    prettyPrint()
                ),
                preprocessResponse(
                    modifyHeaders().removeMatching("^(?!%s$).*".formatted(AUTHORIZATION_HEADER)),
                    modifyHeaders().set(AUTHORIZATION_HEADER, TOKEN_PREFIX + "{{ACCESS_TOKEN}}"),
                    prettyPrint()
                ),
                requestFields(
                    fieldWithPath("username")
                        .type(JsonFieldType.STRING)
                        .description("아이디"),
                    fieldWithPath("password")
                        .type(JsonFieldType.STRING)
                        .description("비밀번호")),
                responseHeaders(
                    headerWithName(JwtTokenGenFilter.AUTHORIZATION_HEADER)
                        .description("인증 토큰을 포함하는 헤더")
                )
            ));
  }
}
