package com.example.Common.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AccountRole")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountRole {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "AccountId")
    @ManyToOne
    private Account account;

    @JoinColumn(name = "RoleId")
    @ManyToOne
    private Role role;
}
