package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.DangKyDTO;
import com.hoainhi.staywise.dtos.DangNhapDTO;
import com.hoainhi.staywise.dtos.DangNhapResponse;
import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.services.serviceimpl.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

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
    public ResponseEntity<?> dangNhap(@RequestBody DangNhapDTO dangNhapDTO){
        try{
            DangNhapResponse dangNhapResponse = authService.dangNhap(dangNhapDTO);
            return ResponseEntity.ok(dangNhapResponse);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    public ResponseEntity<?> dangXuat(
            @RequestHeader(value = "Authorization", required = false, defaultValue="Authorization")
            String authorization){
        if(authorization == null || !authorization.startsWith("Bearer ")){

            return ResponseEntity.status(401)
                    .body(Map.of(
                            "message", "Token không hợp lệ"
                    ));
        }
        String token = authorization.substring(7);
        authService.dangXuat(token);
        return ResponseEntity.ok(
                Map.of("message", "Đăng xuất thành công")
        );
    }
}
