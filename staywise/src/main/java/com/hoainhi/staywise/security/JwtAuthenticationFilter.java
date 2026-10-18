
package com.hoainhi.staywise.security;

import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private  NguoiDungRepository nguoiDungRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lấy token từ Header Authorization
        String authHeader = request.getHeader("Authorization");

        // Không có token thì chuyển request sang filter tiếp theo
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            // 2. Kiểm tra token hợp lệ và còn hạn
            if (!jwtService.kiemTraToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 3. Lấy email từ token
            String email = jwtService.layEmailTuToken(token);

            // 4. Chỉ xác thực nếu request chưa có Authentication
            if (SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                NguoiDung nguoiDung = nguoiDungRepository
                        .findByEmail(email)
                        .orElse(null);

                if (nguoiDung != null) {

                    // 5. Kiểm tra tài khoản còn hoạt động
                    if (nguoiDung.getTrangThaiUser()
                            != com.hoainhi.staywise.enums.TrangThaiUser.DANG_HOAT_DONG) {
                        filterChain.doFilter(request, response);
                        return;
                    }

                    // 6. Đưa vai trò vào Spring Security
                    var authorities = List.of(
                            new SimpleGrantedAuthority(
                                    "ROLE_" + nguoiDung.getVaiTro().name()
                            )
                    );

                    // Principal là email của người đang đăng nhập
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    nguoiDung.getEmail(),
                                    null,
                                    authorities
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // 7. Lưu thông tin xác thực
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (io.jsonwebtoken.JwtException
                 | IllegalArgumentException e) {
            // Token sai chữ ký, hết hạn hoặc không hợp lệ:
            // không xác thực người dùng.
            SecurityContextHolder.clearContext();
        }

        // 8. Tiếp tục chuỗi filter
        filterChain.doFilter(request, response);
    }
}
