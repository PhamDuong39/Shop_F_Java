package com.example.Auth.Dto;

import com.example.Common.Entity.Account;
import com.example.Common.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountData {
    private Account account;
    private List<Role> roleList;
}
