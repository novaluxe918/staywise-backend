package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.DangKyDTO;
import com.hoainhi.staywise.dtos.DangNhapDTO;
import com.hoainhi.staywise.dtos.DangNhapResponse;
import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;
import com.hoainhi.staywise.services.serviceimpl.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/dangKy")
    public ResponseEntity<?> dangky(@Valid @RequestBody DangKyDTO dangKyDTO){
       try{
           authService.dangKy(dangKyDTO);
           return ResponseEntity.ok().body("Dang ky thanh cong");
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @PostMapping("/dangNhap")
    public ResponseEntity<?> dangNhap(@RequestBody DangNhapDTO dangNhapDTO) throws Exception {
        DangNhapResponse response = authService.dangNhap(dangNhapDTO);

        // 1. Cookie bảo mật chứa JWT (JavaScript không đọc được)
        ResponseCookie tokenCookie = ResponseCookie.from("access_token", response.token())
                .httpOnly(true)
                .secure(false) // Bắt buộc false trên localhost HTTP
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();

        // 2. Cookie chứa User Info để React đọc khi F5 (httpOnly = false)
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", response.id());
        userInfo.put("email", response.email());
        userInfo.put("vaiTro", response.vaiTro());
        userInfo.put("hoTen", response.hoTen() != null ? response.hoTen() : "");

        String userJson = URLEncoder.encode(objectMapper.writeValueAsString(userInfo), StandardCharsets.UTF_8);

        ResponseCookie userCookie = ResponseCookie.from("user_info", userJson)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, userCookie.toString())
                .body(userInfo);
    }


    @PostMapping("/dang-xuat")
    public ResponseEntity<String> dangXuat() {
        ResponseCookie clearToken = ResponseCookie.from("access_token", "")
                .httpOnly(true).secure(false).path("/").maxAge(0).build();

        ResponseCookie clearUser = ResponseCookie.from("user_info", "")
                .httpOnly(false).secure(false).path("/").maxAge(0).build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearToken.toString())
                .header(HttpHeaders.SET_COOKIE, clearUser.toString())
                .body("Đăng xuất thành công");
    }




}
