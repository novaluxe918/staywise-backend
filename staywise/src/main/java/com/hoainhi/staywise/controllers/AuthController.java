package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.DangKyDTO;
import com.hoainhi.staywise.services.serviceimpl.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
