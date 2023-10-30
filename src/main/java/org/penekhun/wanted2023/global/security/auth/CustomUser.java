package org.penekhun.wanted2023.global.security.auth;

import java.util.Collections;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;
import org.penekhun.wanted2023.user.entity.PersonalUserAccount;
import org.penekhun.wanted2023.user.entity.UserAccount;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Slf4j
@Getter
public class CustomUser extends User {

  private final boolean isPersonalUser;
  private PersonalUserAccount personalUser;
  private EnterpriseUserAccount enterpriseUser;

  public CustomUser(UserAccount account, boolean isPersonalUser) {
    super(account.getUsername(), account.getPassword(),
        Collections.singleton(new SimpleGrantedAuthority("ROLE_" + account.getRole())));
    this.isPersonalUser = isPersonalUser;

    if (isPersonalUser) {
      this.personalUser = (PersonalUserAccount) account;
    } else {
      this.enterpriseUser = (EnterpriseUserAccount) account;
    }
  }

  public UserAccount getAccount() {
    return isPersonalUser ? personalUser : enterpriseUser;
  }

}
