package com.hoainhi.staywise.dtos;

import com.hoainhi.staywise.enums.VaiTro;
import lombok.Data;

@Data
public class DangKyDTO {
    private String hoTen;
    private String email;
    private String matKhau;
    private String soDienThoai;
    private VaiTro vaiTro;
}
