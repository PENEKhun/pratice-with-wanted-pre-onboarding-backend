package org.penekhun.wanted2023.user.repository;

import java.util.Optional;
import org.penekhun.wanted2023.user.entity.EnterpriseUserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterpriseAccountRepository extends JpaRepository<EnterpriseUserAccount, Long> {

  Optional<EnterpriseUserAccount> findByUsername(String username);

}
