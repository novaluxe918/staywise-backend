package com.hoainhi.staywise.dtos;

import com.hoainhi.staywise.validations.ThemMoi;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DichVuDTO {
    private Long id;

    @NotBlank(message = "Không được để trống", groups = ThemMoi.class)
    private String tenDichVu;
}
