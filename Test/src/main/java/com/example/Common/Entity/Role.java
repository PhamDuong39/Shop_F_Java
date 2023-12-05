package com.example.Common.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Entity
@Table(name = "Role")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String roleName;


    @OneToMany(mappedBy = "role")
    @Null
    private List<AccountRole> accountRoles;
}
