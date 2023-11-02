package org.penekhun.wanted2023.global.security.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.penekhun.wanted2023.global.docs.RestDocsSupport;
import org.penekhun.wanted2023.global.security.auth.CustomUser;
import org.penekhun.wanted2023.global.security.auth.CustomUserDetailsService;
import org.penekhun.wanted2023.user.entity.PersonalUserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class JwtTokenGenFilterTest extends RestDocsSupport {

  @MockBean
  CustomUserDetailsService customUserDetailsService;

  @Autowired
  PasswordEncoder passwordEncoder;

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
            .content(
                objectMapper.writeValueAsString(
                    new JwtTokenGenFilter.UserLogin("username", "password")
                )))
        .andDo(print())
        .andDo(
            restDocs.document(
                requestFields(
                    fieldWithPath("username")
                        .type(JsonFieldType.STRING)
                        .description("아이디"),
                    fieldWithPath("password")
                        .type(JsonFieldType.STRING)
                        .description("비밀번호")),
                responseHeaders(
                    headerWithName("Authorization")
                        .description("인증 토큰을 포함하는 헤더")
                )
            ));
  }

  @Override
  protected boolean includesJwtFilter() {
    return true;
  }
}
