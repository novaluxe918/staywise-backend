package com.hoainhi.staywise.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DangNhapResponse {

    private String token;
    private Long id;
    private String email;
    private String vaiTro;
}
