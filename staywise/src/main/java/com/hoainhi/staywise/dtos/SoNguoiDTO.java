package com.hoainhi.staywise.dtos;

import com.hoainhi.staywise.enums.TheLoaiNguoi;
import lombok.Data;

@Data
public class SoNguoiDTO {
    private Long id;

    private int soLuong;

    private TheLoaiNguoi theLoaiNguoi;
}
