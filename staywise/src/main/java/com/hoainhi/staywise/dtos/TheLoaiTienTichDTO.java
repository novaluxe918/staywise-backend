package com.hoainhi.staywise.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TheLoaiTienTichDTO {

    private Long id;
    @NotBlank(message = "Không được để trống")
    private String tenTheLoai;

    private String hinhAnh;


}
