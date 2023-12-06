package com.example.Auth.Service.Imp;

import com.example.Auth.Dto.ConfirmEmailDto;
import com.example.Auth.Dto.LoginDto;
import com.example.Auth.Dto.RegisterDto;
import com.example.Auth.Dto.ResetPasswordDto;
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
import com.example.Email.Dto.DataMailDto;
import com.example.Email.Service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final MailService mailService;

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

            // save role of user
            Optional<Role> optionalRole = roles.stream()
                    .filter(role -> "USER".equals(role.getRoleName())).findFirst();
            Role role = optionalRole.get();

            AccountRole accountRole = new AccountRole();
            accountRole.setAccount(account);
            accountRole.setRole(role);
            var savedAccountRole = accountRoleRepository.save(accountRole);

            // Send Email Confirmation Email
            DataMailDto dataMailDto = new DataMailDto();
            dataMailDto.setTo(savedAccount.getEmail());
            dataMailDto.setSubject("XÁC NHẬN EMAIL NGƯỜI DÙNG");

            Map<String, Object> props = new HashMap<>();
            props.put("fullName", savedAccount.getFullname());
            props.put("email", savedAccount.getEmail());

            String tokenConfirm = jwtService.GenerateEmailConfirmToken(savedAccount);
            String confirmationLink =
                    "http://localhost:8080/api/v1/auth/confirm-email?email=" + savedAccount.getEmail() + "&confirmToken=" + tokenConfirm;
            props.put("confirmationLink", confirmationLink);
            dataMailDto.setProps(props);

            mailService.sendHtmlEmail(dataMailDto, "EmailTemplate");

            // return
            ResponseModel<Account> response = new ResponseModel<>();
            response.setSuccess(true);
            response.setMessage("Account has been created successfully!! and Email has been sent");
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
    public ResponseModel<ConfirmEmailDto> ConfirmEmail(ConfirmEmailDto confirmEmailDto) {
        // check pattern email
        boolean isEmail = isEmailCorrectPattern(confirmEmailDto.getEmail());
        if(!isEmail) {
            ResponseModel<ConfirmEmailDto> response = new ResponseModel<>();
            response.setSuccess(false);
            response.setMessage("Email isn't correct!!");
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }

        // get account by email
        Optional<Account> optional = accountRepository.findByEmail(confirmEmailDto.getEmail());
        if(optional.isPresent()) {
            Account account = optional.get();
            boolean checkResult = jwtService.isTokenValid(confirmEmailDto.getConfirmToken(), account);
            if(!checkResult) {
                ResponseModel<ConfirmEmailDto> response = new ResponseModel<>();
                response.setSuccess(false);
                response.setMessage("Email hasn't been registered!!");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }

            account.setUpdatedAt(LocalDate.now());
            account.setEmailConfirmed(true);

            Account updatedAccount = accountRepository.save(account);

            ResponseModel<ConfirmEmailDto> response = new ResponseModel<>();
            response.setSuccess(true);
            response.setMessage("Email hasn been confirm successfully!!");
            response.setStatusCode(HttpStatus.OK.value());
            return response;
        }
        ResponseModel<ConfirmEmailDto> response = new ResponseModel<>();
        response.setSuccess(false);
        response.setMessage("Confirm email failed!!!");
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return response;
    }


    @Override
    public ResponseModel<ResetPasswordDto> ForgotPassword(String email) throws MessagingException {
        // check email, If exists => return BadRequest();
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if(optionalAccount.isPresent()) {
           // if email is exists
            Account account = optionalAccount.get();

            DataMailDto dataMailDto = new DataMailDto();
            dataMailDto.setTo(account.getEmail());
            dataMailDto.setSubject("THAY ĐỔI PASSWORD");

            Map<String, Object> props = new HashMap<>();
            props.put("fullName", account.getFullname());
            props.put("email", account.getEmail());

            String tokenConfirm = jwtService.GeneratePasswordResetToken(account);
            String confirmationLink =
                    "http://localhost:8080/api/v1/auth/change-password?email=" + account.getEmail() + "&resetToken=" + tokenConfirm;
            props.put("confirmationLink", confirmationLink);
            dataMailDto.setProps(props);

            mailService.sendHtmlEmail(dataMailDto, "EmailTemplate");


            ResponseModel<ResetPasswordDto> response = new ResponseModel<>();
            response.setSuccess(true);
            response.setMessage(" Email change password has been sent");
            response.setStatusCode(HttpStatus.OK.value());
            return response;
        }
        ResponseModel<ResetPasswordDto> response = new ResponseModel<>();
        response.setSuccess(false);
        response.setMessage(" Email isn't exists");
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return response;
    }

    @Override
    public ResponseModel<ResetPasswordDto> ChangePassword(ResetPasswordDto resetPasswordDto) {
        Optional<Account> optionalAccount = accountRepository.findByEmail(resetPasswordDto.getEmail());
        if(optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            boolean checkResult = jwtService.isTokenValid(resetPasswordDto.getToken(), account);
            if(!checkResult) {
                ResponseModel<ResetPasswordDto> response = new ResponseModel<>();
                response.setSuccess(false);
                response.setMessage("Email hasn't been registered!!");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }

            account.setUpdatedAt(LocalDate.now());
            account.setPasswordHash(passwordEncoder.encode(resetPasswordDto.getNewPassword()));

            Account updatedAccount = accountRepository.save(account);

            ResponseModel<ResetPasswordDto> response = new ResponseModel<>();
            response.setSuccess(true);
            response.setMessage("Password has been changed successfully!!");
            response.setData(resetPasswordDto);
            response.setStatusCode(HttpStatus.OK.value());
            return response;
        }
        ResponseModel<ResetPasswordDto> response = new ResponseModel<>();
        response.setSuccess(false);
        response.setMessage(" Email isn't exists");
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return response;
    }


    @Override
    public ResponseModel<AuthReponse> Login(LoginDto loginDto) {
        try{
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            // bug here
            authenticationManager.authenticate(authenticationToken);
            var account = accountRepository.findByEmail(loginDto.getEmail()).orElseThrow();

            if(!account.isEmailConfirmed()) {
                ResponseModel<AuthReponse> response = new ResponseModel<>();
                response.setSuccess(false);
                response.setMessage("Email isn't confirm!!! Please confirm your email to access the data");
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return response;
            }

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


    private boolean isEmailCorrectPattern(String email) {
        // check pattern email
        Pattern pattern = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
