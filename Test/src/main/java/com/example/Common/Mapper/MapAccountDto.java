package com.example.Common.Mapper;

import com.example.Auth.Dto.RegisterDto;
import com.example.Common.Entity.Account;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapAccountDto {
    public static Account MapRegisterDtoToAccount(RegisterDto registerDto) {
        return Account.builder()
                .fullname(registerDto.getFullName())
                .email(registerDto.getEmail())
                .sDT(registerDto.getSdt())
                .build();
    }
}
