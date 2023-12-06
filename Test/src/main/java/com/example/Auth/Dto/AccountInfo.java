package com.example.Auth.Dto;


import com.example.Auth.Repository.RoleRepository;
import com.example.Common.Entity.Account;
import com.example.Common.Entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//@AllArgsConstructor
public class AccountInfo implements UserDetails {

    private final String Email;
    private final String Password;
    private final List<GrantedAuthority> authorities;

    // maybe die here
    private RoleRepository roleRepository;

    // tạo ra 1 thằng là accoountData(Account, List<Quyen>)
    // Viết 1 câu query trong repo để get all quyền from account ID
    // chô nàu truyền vào accountData => xong get nó ra là xong
    public AccountInfo(AccountData accountData) {
        Email = accountData.getAccount().getEmail();
        Password = accountData.getAccount().getPasswordHash();

        String[] roles = new String[accountData.getRoleList().size()];
        int  i = 0;
        for(Role role : accountData.getRoleList()) {
            roles[i] = role.getRoleName();
            i++;
        }

        authorities = Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return Password;
    }

    @Override
    public String getUsername() {
        return Email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
