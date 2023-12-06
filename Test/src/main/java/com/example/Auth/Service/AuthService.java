package com.example.Auth.Service;


import com.example.Auth.Dto.ConfirmEmailDto;
import com.example.Auth.Dto.LoginDto;
import com.example.Auth.Dto.RegisterDto;
import com.example.Auth.Dto.ResetPasswordDto;
import com.example.Common.Entity.Account;
import com.example.Common.Response.AuthReponse;
import com.example.Common.Response.ResponseModel;
import jakarta.mail.MessagingException;
import org.apache.coyote.Response;

public interface AuthService {
    ResponseModel<Account> Register(RegisterDto registerDto);
    ResponseModel<ConfirmEmailDto> ConfirmEmail(ConfirmEmailDto confirmEmailDto);

    ResponseModel<ResetPasswordDto> ForgotPassword(String email) throws MessagingException;
    ResponseModel<ResetPasswordDto> ChangePassword(ResetPasswordDto resetPasswordDto);

    ResponseModel<AuthReponse> Login(LoginDto loginDto);


}
