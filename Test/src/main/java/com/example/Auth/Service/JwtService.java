package com.example.Auth.Service;

import com.example.Common.Entity.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    @Value("${JWT.secretKey}")
    private String secretKey;

    @Value("${JWT.jwtExpiration}")
    private long jwtExpiration;

    @Value("${JWT.refreshExpiration}")
    private long refreshExpiration;

    // Build + Create token

    private String BuildToken(Map<String, Object> extraClaims, Account account, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(account.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(GetSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key GetSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String GenerateToken(Map<String, Object> extraClaims, Account account) {
        //extraClaims.put("Role", account.get);
        return BuildToken(extraClaims, account, jwtExpiration);
    }

    public String GenerateAccessToken(Account account) {
        return GenerateToken(new HashMap<>(), account);
    }

    public String GenerateRefreshToken(Account account) {
        return BuildToken(new HashMap<>(), account, refreshExpiration);
    }



    // Read + get data in token
    private Claims ExtractAllClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(GetSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T  ExtractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = ExtractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    public String ExtractUsername(String token) {
        return ExtractClaim(token, Claims::getSubject);
    }



    // Validate token

    private Date ExtractExpiration(String token) {
        return ExtractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return ExtractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = ExtractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
