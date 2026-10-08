package com.hoainhi.staywise.dtos;

import com.hoainhi.staywise.enums.TheLoaiHomeStay;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class HomeStayDTO {
    private Long maChuHomeStay;

    @NotBlank(message = "Tên HomeStay không được để trống")
    private String tenHomeStay;

    private String moTa;

    @NotBlank(message = "Phường/Xã không được để trống")
    private String phuongXa;

    @NotBlank(message = "Quận/Huyện không được để trống")
    private String quanHuyen;

    @NotBlank(message = "Tỉnh/Thành phố không được để trống")
    private String tinhThanh;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String diaChi;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(
            regexp = "^(0[0-9]{9})$",
            message = "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0"
    )
    private String soDienThoai;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    private TheLoaiHomeStay theLoaiHomeStay;

    private LocalDate ngayDangKy;

    private List<Long> danhSachTienIchHS;

    private List<Long> danhSachDichVuHS;
}
