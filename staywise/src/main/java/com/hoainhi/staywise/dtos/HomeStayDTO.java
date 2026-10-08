package com.hoainhi.staywise.dtos;

import com.hoainhi.staywise.enums.TheLoaiHomeStay;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class HomeStayDTO {
    private String tenHomeStay;

    private String moTa;

    private String phuongXa;

    private String quanHuyen;

    private String tinhThanh;

    private String diaChi;

    private String soDienThoai;

    private String email;

    private TheLoaiHomeStay theLoaiHomeStay;

    private LocalDate ngayDangKy;

    private List<Long> danhSachTienIchHS;
}
