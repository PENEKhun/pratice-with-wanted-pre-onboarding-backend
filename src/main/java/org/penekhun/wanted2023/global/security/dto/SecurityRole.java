package org.penekhun.wanted2023.global.security.dto;

import org.springframework.security.core.GrantedAuthority;

public enum SecurityRole implements GrantedAuthority {

  USER(ROLES.USER, "일반 사용자"),
  ENTERPRISE(ROLES.ENTERPRISE, "기업 사용자"),
  ;

  private final String role;
  private final String description;

  SecurityRole(String role, String description) {
    this.role = role;
    this.description = description;
  }

  @Override
  public String getAuthority() {
    return role;
  }

  /**
   * Controller @Secured annotation 에서 사용하기 위해 만든 클래스
   */
  public static class ROLES {

    public static final String USER = "ROLE_USER";
    public static final String ENTERPRISE = "ROLE_ENTERPRISE";
    
    public static class WITHOUT_PREFIX {

      public static final String USER = "USER";
      public static final String ENTERPRISE = "ENTERPRISE";
    }
  }
}
