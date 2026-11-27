package com.hoainhi.staywise.security;

import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lấy token từ HttpOnly Cookie
        String token = layTokenTuCookie(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 2. Kiểm tra token hợp lệ và còn hạn
            if (!jwtService.kiemTraToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 3. Lấy email từ token
            String email = jwtService.layEmailTuToken(token);

            // 4. Luôn ưu tiên xác thực theo token mới nhất (không giữ session cũ)
            NguoiDung nguoiDung = nguoiDungRepository
                    .findByEmail(email)
                    .orElse(null);

            if (nguoiDung != null) {
                if (nguoiDung.getTrangThaiUser()
                        != com.hoainhi.staywise.enums.TrangThaiUser.DANG_HOAT_DONG) {
                    filterChain.doFilter(request, response);
                    return;
                }

                var authorities = List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + nguoiDung.getVaiTro().name()
                        )
                );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                nguoiDung.getEmail(),
                                null,
                                authorities
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String layTokenTuCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("access_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}