package com.hoainhi.staywise.dtos;

import com.hoainhi.staywise.validations.ThemMoi;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TienIchDTO {
    private Long id;

    @NotBlank(message = "Không được để trống", groups = ThemMoi.class)
    private String tenTienIch;

    private String moTa;

    private LocalDateTime ngayTao;

    @NotNull(message = "Thể loại không được để trống", groups = ThemMoi.class)
    private Long matheloai;

}
