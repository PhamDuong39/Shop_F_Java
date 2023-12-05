package com.example.Auth.Repository;

import com.example.Common.Entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
}
