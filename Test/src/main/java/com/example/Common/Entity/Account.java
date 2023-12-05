package com.example.Common.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Account")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private boolean emailConfirmed;
    private String passwordHash;
    private String fullname;
    private String sDT;
    private boolean status;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private String refreshToken;
    private LocalDate refreshTokenExpiryTile;

    @OneToMany(mappedBy = "account")
    private List<AccountRole> accountRoles;

    @OneToMany(mappedBy = "account")
    private List<Order> orders;

    @OneToMany(mappedBy = "account")
    private List<Cart> carts;
}
