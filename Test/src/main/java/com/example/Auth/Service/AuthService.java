package com.example.Auth.Service;


import com.example.Auth.Dto.LoginDto;
import com.example.Auth.Dto.RegisterDto;
import com.example.Common.Entity.Account;
import com.example.Common.Response.AuthReponse;
import com.example.Common.Response.ResponseModel;

public interface AuthService {
    ResponseModel<Account> Register(RegisterDto registerDto);
    ResponseModel<AuthReponse> Login(LoginDto loginDto);
}
