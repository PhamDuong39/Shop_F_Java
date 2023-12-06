package com.example.Auth.Repository;

import com.example.Common.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r " +
            "JOIN AccountRole ar ON r.id = ar.role.id " +
            "JOIN Account a ON a.id = ar.account.id " +
            "WHERE a.id = :accountId")
    List<Role> findRolesByEmail(Long accountId);
}
