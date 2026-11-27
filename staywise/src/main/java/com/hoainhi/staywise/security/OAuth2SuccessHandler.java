
package com.hoainhi.staywise.security;

import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.enums.TrangThaiUser;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final NguoiDungRepository nguoiDungRepository;
    private final ObjectMapper objectMapper;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    public OAuth2SuccessHandler(
            JwtService jwtService,
            NguoiDungRepository nguoiDungRepository,
            ObjectMapper objectMapper
    ) {
        this.jwtService = jwtService;
        this.nguoiDungRepository = nguoiDungRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        // 1. Lấy thông tin Google
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        String email = oidcUser.getEmail()
                .trim()
                .toLowerCase();

        // 2. Tìm người dùng trong database
        NguoiDung nguoiDung = nguoiDungRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Không tìm thấy người dùng"
                        )
                );

        // 3. Kiểm tra quyền và trạng thái
        if (nguoiDung.getVaiTro() != VaiTro.USER
                || nguoiDung.getTrangThaiUser()
                != TrangThaiUser.DANG_HOAT_DONG) {

            response.sendRedirect(
                    frontendUrl + "/dang-nhap?error=google_login_failed"
            );
            return;
        }

        // 4. Tạo JWT
        String token = jwtService.taoToken(email);

        // 5. Tính thời gian hết hạn
        LocalDateTime hetHan =
                jwtService.layThoiDiemHetHan(token);

        Duration maxAge = Duration.between(
                LocalDateTime.now(),
                hetHan
        );

        if (maxAge.isNegative() || maxAge.isZero()) {
            maxAge = Duration.ZERO;
        }

        // 6. Tạo cookie access_token
        ResponseCookie accessCookie = ResponseCookie
                .from("access_token", token)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAge)
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                accessCookie.toString()
        );

        // 7. Tạo dữ liệu user_info cho Frontend
        Map<String, Object> userInfo = new LinkedHashMap<>();

        userInfo.put("id", nguoiDung.getId());
        userInfo.put("email", nguoiDung.getEmail());
        userInfo.put("vaiTro", nguoiDung.getVaiTro());
        userInfo.put("hoTen", nguoiDung.getHoTen());
        userInfo.put("anhDaiDien", nguoiDung.getAnhDaiDien());

        String jsonUser = objectMapper.writeValueAsString(userInfo);

        String encodedUser = URLEncoder
                .encode(jsonUser, StandardCharsets.UTF_8)
                .replace("+", "%20");

        // 8. Tạo cookie user_info
        ResponseCookie userCookie = ResponseCookie
                .from("user_info", encodedUser)
                .httpOnly(false)
                .secure(cookieSecure)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAge)
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                userCookie.toString()
        );

        // 9. Xóa session OAuth2 tạm thời
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        // 10. Chuyển về Frontend
        response.sendRedirect(frontendUrl + "/");
    }
}
