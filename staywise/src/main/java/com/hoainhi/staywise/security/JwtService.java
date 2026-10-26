
package com.hoainhi.staywise.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {

        byte[] keyBytes = Base64.getDecoder().decode(secret);

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expiration = expiration;
    }

    // Tạo JWT từ email người dùng
    public String taoToken(String email) {
        Date now = new Date();
        Date hetHan = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(hetHan)
                .signWith(secretKey)
                .compact();
    }

    // Đọc email từ JWT
    public String layEmailTuToken(String token) {
        return layClaims(token).getSubject();
    }

    // Kiểm tra chữ ký và thời hạn JWT
    public boolean kiemTraToken(String token) {
        try {
            layClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Giải mã và xác thực JWT
    private Claims layClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public LocalDateTime layThoiDiemHetHan(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }


}
