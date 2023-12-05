package com.example.Auth.Controller;

import com.example.Auth.Dto.LoginDto;
import com.example.Auth.Dto.RegisterDto;
import com.example.Auth.Service.AuthService;
import com.example.Common.Entity.Account;
import com.example.Common.Response.AuthReponse;
import com.example.Common.Response.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public ResponseEntity<ResponseModel<AuthReponse>> login(
            @RequestBody LoginDto loginDto
    ) {
        var result = authService.Login(loginDto);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
