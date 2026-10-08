package com.hoainhi.staywise.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TienIchDTO {
    private Long id;

    @NotBlank(message = "Không được để trống")
    private String tenTienIch;

    private String moTa;
    private LocalDateTime ngayTao;
    private Long matheloai;

}
