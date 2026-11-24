package com.hoainhi.staywise.response;


public record DangNhapResponse(
        String token,
        Long id,
        String email,
        String vaiTro,
        String hoTen,
        String anhDaiDien
) {}