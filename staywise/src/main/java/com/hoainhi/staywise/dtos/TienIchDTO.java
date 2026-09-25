package com.hoainhi.staywise.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TienIchDTO {
    private Long id;
    private String tenTienIch;
    private String moTa;
    private LocalDateTime ngayTao;
    private Long matheloai;

}
