
package com.hoainhi.staywise.security;

import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.enums.TrangThaiUser;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends OidcUserService {

    private final NguoiDungRepository nguoiDungRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(
            NguoiDungRepository nguoiDungRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.nguoiDungRepository = nguoiDungRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest)
            throws OAuth2AuthenticationException {

        // Lấy thông tin người dùng từ Google
        OidcUser googleUser = super.loadUser(userRequest);

        String email = googleUser.getAttribute("email");
        String hoTen = googleUser.getAttribute("name");
        String anhDaiDien = googleUser.getAttribute("picture");
        Boolean emailVerified = googleUser.getAttribute("email_verified");

        // Kiểm tra email Google
        if (email == null || email.isBlank()
                || !Boolean.TRUE.equals(emailVerified)) {

            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_email"),
                    "Email Google chưa được xác minh"
            );
        }

        email = email.trim().toLowerCase(Locale.ROOT);

        // Tìm tài khoản theo email
        NguoiDung nguoiDung = nguoiDungRepository
                .findByEmail(email)
                .orElse(null);

        // Nếu chưa tồn tại thì tạo tài khoản USER
        if (nguoiDung == null) {

            nguoiDung = new NguoiDung();

            nguoiDung.setEmail(email);

            nguoiDung.setHoTen(
                    hoTen != null && !hoTen.isBlank()
                            ? hoTen
                            : email
            );

            // Tạo mật khẩu ngẫu nhiên, không dùng mật khẩu mặc định
            nguoiDung.setMatKhau(
                    passwordEncoder.encode(UUID.randomUUID().toString())
            );

            nguoiDung.setVaiTro(VaiTro.USER);
            nguoiDung.setTrangThaiUser(TrangThaiUser.DANG_HOAT_DONG);
            nguoiDung.setAnhDaiDien(anhDaiDien);
            nguoiDung.setNgayTao(LocalDateTime.now());

            nguoiDung = nguoiDungRepository.save(nguoiDung);
        }

        // Không cho phép OWNER và ADMIN đăng nhập Google
        if (nguoiDung.getVaiTro() != VaiTro.USER) {

            throw new OAuth2AuthenticationException(
                    new OAuth2Error("access_denied"),
                    "Chỉ tài khoản USER được đăng nhập bằng Google"
            );
        }

        // Kiểm tra trạng thái tài khoản
        if (nguoiDung.getTrangThaiUser()
                != TrangThaiUser.DANG_HOAT_DONG) {

            throw new OAuth2AuthenticationException(
                    new OAuth2Error("access_denied"),
                    "Tài khoản không hoạt động"
            );
        }

        // Gán quyền USER
        List<GrantedAuthority> authorities =
                new ArrayList<>(googleUser.getAuthorities());

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Trả về thông tin xác thực OIDC
        return new DefaultOidcUser(
                authorities,
                googleUser.getIdToken(),
                googleUser.getUserInfo(),
                "sub"
        );
    }
}
