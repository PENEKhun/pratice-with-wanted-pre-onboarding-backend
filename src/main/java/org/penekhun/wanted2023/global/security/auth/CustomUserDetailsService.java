package org.penekhun.wanted2023.global.security.auth;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;
import org.penekhun.wanted2023.user.entity.PersonalUserAccount;
import org.penekhun.wanted2023.user.entity.UserAccount;
import org.penekhun.wanted2023.user.repository.EnterpriseAccountRepository;
import org.penekhun.wanted2023.user.repository.PersonalAccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final PersonalAccountRepository personalAccountRepository;
  private final EnterpriseAccountRepository enterpriseAccountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("called loadUserByUsername. username: {}", username);

    Optional<EnterpriseUserAccount> foundEnterpriseUser = enterpriseAccountRepository.findByUsername(
        username);
    Optional<PersonalUserAccount> foundPersonalUser = personalAccountRepository.findByUsername(
        username);

    UserAccount userAccount = null;
    if (foundPersonalUser.isPresent()) {
      userAccount = foundPersonalUser.get();

    } else if (foundEnterpriseUser.isPresent()) {
      userAccount = foundEnterpriseUser.get();
    } else {
      throw new EntityNotFoundException("존재하지 않는 회원입니다.");
    }

    return new CustomUser(userAccount,
        foundPersonalUser.isPresent(),
        Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + userAccount.getRole().toUpperCase())));
  }
}
