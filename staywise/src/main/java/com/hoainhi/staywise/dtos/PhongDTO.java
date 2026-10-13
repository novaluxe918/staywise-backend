package com.hoainhi.staywise.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PhongDTO {
    private Long id;

    private Long maHomeStay;

    @NotBlank(message = "Ten phong khong duoc de trong")
    private String tenPhong;

    private String moTa;

    @NotNull(message = "Diện tích không được để trống")
    @DecimalMin(value = "0.1", message = "Diện tích phải lớn hơn 0")
    private BigDecimal dienTich;

    @NotBlank(message = "Loai phong khong duoc de trong")
    private String loaiPhong;

    private List<ChiTietGiuongDTO> danhSachGiuong;
}
