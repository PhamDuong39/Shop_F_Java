package com.example.Auth.Controller;

import com.example.Auth.Dto.ConfirmEmailDto;
import com.example.Auth.Dto.LoginDto;
import com.example.Auth.Dto.RegisterDto;
import com.example.Auth.Dto.ResetPasswordDto;
import com.example.Auth.Service.AuthService;
import com.example.Common.Entity.Account;
import com.example.Common.Response.AuthReponse;
import com.example.Common.Response.ResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "Register a new account", description = "API to register a new user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseModel<Account>> register(
            @RequestBody RegisterDto registerDto
    ) {
        var result = authService.Register(registerDto);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }



    @Operation(summary = "Confirm user's email address", description = "API to confirm user's email address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
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


    @Operation(summary = "Forgot Password", description = "API for initiating the password reset process.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent for password reset"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseModel<ResetPasswordDto>> ForgotPass(
            @RequestBody String email
    ) throws MessagingException {
        var result = authService.ForgotPassword(email);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }


    @Operation(summary = "Change Password", description = "API for changing the user's password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PutMapping("/change-password")
    public ResponseEntity<ResponseModel<ResetPasswordDto>> ChangePass(
            @RequestBody ResetPasswordDto resetPasswordDto
    ) {
        var result = authService.ChangePassword(resetPasswordDto);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }


    @Operation(summary = "User login", description = "API for user login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseModel<AuthReponse>> login(
            @RequestBody LoginDto loginDto
    ) {
        var result = authService.Login(loginDto);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
