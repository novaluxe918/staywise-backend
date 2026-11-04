package com.hoainhi.staywise.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;


public record DangNhapResponse(
        String token,
        Long id,
        String email,
        String vaiTro,
        String hoTen
) {}