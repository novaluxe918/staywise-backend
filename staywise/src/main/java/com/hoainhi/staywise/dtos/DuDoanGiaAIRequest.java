package com.hoainhi.staywise.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DuDoanGiaAIRequest {
    private Long maPhong;

    private LocalDate ngayApDung;

    private double giaHienTai;

    private double dienTich;

    private int soNguoi;

    private String loaiPhong;

    private String tinhThanh;
}
