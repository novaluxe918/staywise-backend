package com.hoainhi.staywise.dtos;

import com.hoainhi.staywise.validations.CapNhat;
import com.hoainhi.staywise.validations.ThemMoi;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PhongDTO {
    private Long id;

    private Long maHomeStay;

    @NotBlank(message = "Ten phong khong duoc de trong", groups = {ThemMoi.class})
    private String tenPhong;

    private String moTa;

    @NotNull(message = "Diện tích không được để trống", groups = {ThemMoi.class})
    @DecimalMin(value = "0.1", message = "Diện tích phải lớn hơn 0")
    private BigDecimal dienTich;

    @NotBlank(message = "Loai phong khong duoc de trong", groups = {ThemMoi.class})
    private String loaiPhong;

    private List<ChiTietGiuongDTO> danhSachGiuong = new ArrayList<>();

    private List<SoNguoiDTO> danhSachSoNguoi = new ArrayList<>();
}
