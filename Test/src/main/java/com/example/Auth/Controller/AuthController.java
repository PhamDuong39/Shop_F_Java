package com.example.Auth.Controller;

import com.example.Auth.Dto.ConfirmEmailDto;
import com.example.Auth.Dto.LoginDto;
import com.example.Auth.Dto.RegisterDto;
import com.example.Auth.Dto.ResetPasswordDto;
import com.example.Auth.Service.AuthService;
import com.example.Common.Entity.Account;
import com.example.Common.Response.AuthReponse;
import com.example.Common.Response.ResponseModel;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseModel<Account>> register(
            @RequestBody RegisterDto registerDto
    ) {
        var result = authService.Register(registerDto);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<ResponseModel<ConfirmEmailDto>> ConfirmEmail(
            @RequestParam String email,
            @RequestParam String confirmToken
    ) {
        ConfirmEmailDto confirmEmailDto = new ConfirmEmailDto();
        confirmEmailDto.setEmail(email);
        confirmEmailDto.setConfirmToken(confirmToken);
        var result = authService.ConfirmEmail(confirmEmailDto);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseModel<ResetPasswordDto>> ForgotPass(
            @RequestBody String email
    ) throws MessagingException {
        var result = authService.ForgotPassword(email);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ResponseModel<ResetPasswordDto>> ChangePass(
            @RequestBody ResetPasswordDto resetPasswordDto
    ) {
        var result = authService.ChangePassword(resetPasswordDto);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseModel<AuthReponse>> login(
            @RequestBody LoginDto loginDto
    ) {
        var result = authService.Login(loginDto);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
