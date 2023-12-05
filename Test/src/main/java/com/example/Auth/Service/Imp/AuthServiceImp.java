package com.example.Auth.Service.Imp;


import com.example.Auth.Dto.LoginDto;
import com.example.Auth.Dto.RegisterDto;
import com.example.Auth.Repository.AccountRepository;
import com.example.Auth.Repository.AccountRoleRepository;
import com.example.Auth.Repository.RoleRepository;
import com.example.Auth.Service.AuthService;
import com.example.Auth.Service.JwtService;
import com.example.Common.Entity.Account;
import com.example.Common.Entity.AccountRole;
import com.example.Common.Entity.Role;
import com.example.Common.Mapper.MapAccountDto;
import com.example.Common.Response.AuthReponse;
import com.example.Common.Response.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AccountRoleRepository accountRoleRepository;

    //private final TransactionTemplate transactionTemplate;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ResponseModel<Account> Register(RegisterDto registerDto) {
        try{
            // check email, If exists => return BadRequest();
            var accounts = accountRepository.findAll();
            if(accounts.stream().anyMatch(account -> account.getEmail().equals(registerDto.getEmail()))) {
                ResponseModel<Account> response = new ResponseModel<>();
                response.setSuccess(false);
                response.setMessage("Email is already Exists !!!!");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }

            // Save Account
            var account = MapAccountDto.MapRegisterDtoToAccount(registerDto);
            account.setPasswordHash(passwordEncoder.encode(registerDto.getPassword()));
            account.setCreatedAt(LocalDate.now());
            account.setUpdatedAt(LocalDate.now());
            account.setEmailConfirmed(false);
            account.setRefreshToken(null);
            account.setStatus(false);

            var savedAccount = accountRepository.save(account);

            // Check and Save role of user
            List<Role> roles = roleRepository.findAll();

            List<String> roleNamesToCheck = List.of("USER", "EMPLOYEE", "ADMIN");

            for (String roleName : roleNamesToCheck) {
                boolean roleExists = roles.stream().anyMatch(role -> role.getRoleName().equals(roleName));

                if (!roleExists) {
                    Role newRole = new Role();
                    newRole.setRoleName(roleName);
                    roleRepository.save(newRole);
                }
            }

            Optional<Role> optionalRole = roles.stream()
                    .filter(role -> "USER".equals(role.getRoleName())).findFirst();
            Role role = optionalRole.get();

            AccountRole accountRole = new AccountRole();
            accountRole.setAccount(account);
            accountRole.setRole(role);
            var savedAccountRole = accountRoleRepository.save(accountRole);

            // return
            ResponseModel<Account> response = new ResponseModel<>();
            response.setSuccess(true);
            response.setMessage("Account has been created successfully!!");
            response.setData(savedAccount);
            response.setStatusCode(HttpStatus.OK.value());
            return response;
        }
        catch (Exception ex) {
            ResponseModel<Account> response = new ResponseModel<>();
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return response;
        }
    }

    @Override
    public ResponseModel<AuthReponse> Login(LoginDto loginDto) {
        try{
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            // bug here
            authenticationManager.authenticate(authenticationToken);
            var account = accountRepository.findByEmail(loginDto.getEmail()).orElseThrow();

            var refreshToken = jwtService.GenerateRefreshToken(account);
            var accessToken = jwtService.GenerateAccessToken(account);

            account.setRefreshToken(refreshToken);
            accountRepository.save(account);

            AuthReponse authResponse = new AuthReponse();
            authResponse.setRefreshToken(refreshToken);
            authResponse.setAccessToken(accessToken);
            authResponse.setExpiration(null);

            ResponseModel<AuthReponse> response = new ResponseModel<>();
            response.setSuccess(true);
            response.setMessage("Login successfully!!");
            response.setData(authResponse);
            response.setStatusCode(HttpStatus.OK.value());
            return response;
        }
        catch(Exception ex) {
            ResponseModel<AuthReponse> response = new ResponseModel<>();
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return response;
        }
    }
}
