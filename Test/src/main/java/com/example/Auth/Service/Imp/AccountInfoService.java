package com.example.Auth.Service.Imp;

import com.example.Auth.Dto.AccountData;
import com.example.Auth.Dto.AccountInfo;
import com.example.Auth.Repository.AccountRepository;
import com.example.Auth.Repository.RoleRepository;
import com.example.Common.Entity.Account;
import com.example.Common.Entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountInfoService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByEmail(username);
        List<Role> roles = roleRepository.findRolesByEmail(account.get().getId());

        AccountData accountData = new AccountData(account.get(), roles);
        Optional<AccountData> optionalCheck = Optional.of(accountData);

        return optionalCheck.map(AccountInfo::new)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    }
}
