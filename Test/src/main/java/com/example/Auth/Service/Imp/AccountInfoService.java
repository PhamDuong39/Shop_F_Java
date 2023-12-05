package com.example.Auth.Service.Imp;

import com.example.Auth.Dto.AccountInfo;
import com.example.Auth.Repository.AccountRepository;
import com.example.Common.Entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountInfoService implements UserDetailsService {
    private final AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByEmail(username);

        return account.map(AccountInfo::new)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    }
}
