package com.example.Auth.Config;

import com.example.Auth.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component // đánh dấu class là 1 spring bean được quản lý bởi container IoC cua Spring
@RequiredArgsConstructor // tạo ctor chứa các trường dữ lệu = key final
public class JwtAuthFillter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;// die

    // đăng ký AccountInfoService implement  UserDetailsService

    // kiểm tra header của request để lấy token
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException
    {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // AuthHeader có token (Bearer)-> get token + đọc token -> lấy username
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            email    = jwtService.ExtractUsername(token);
        }

        // Email được tìm thấy
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            var isTokenValid = jwtService.isTokenValid(token, userDetails);

            // Nếu token hợp lệ -> tạo authenticationToken chứa thông tin user -> lưu vào SecurityContextHolder : đánh dấu đã xác thực
            if(isTokenValid) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        // Chuyển request đến các fillter tiếp theo
        filterChain.doFilter(request, response);
    }
}
