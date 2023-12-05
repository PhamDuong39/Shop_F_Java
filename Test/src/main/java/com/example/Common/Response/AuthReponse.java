package com.example.Common.Response;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthReponse {
    @Null
    private String AccessToken;
    @Null
    private String RefreshToken;
    @Null
    private LocalDate Expiration;
}
