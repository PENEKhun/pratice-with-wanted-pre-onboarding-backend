package org.penekhun.wanted2023.user.repository;

import java.util.Optional;
import org.penekhun.wanted2023.user.entity.PersonalUserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalAccountRepository extends JpaRepository<PersonalUserAccount, Long> {

  Optional<PersonalUserAccount> findByUsername(String username);

}
