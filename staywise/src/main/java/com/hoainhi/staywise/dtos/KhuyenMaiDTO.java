package com.hoainhi.staywise.dtos;

import com.hoainhi.staywise.enums.LoaiGiam;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class KhuyenMaiDTO {
    private Long id;
    private String tenKhuyenMai;
    private LoaiGiam loaiGiam;
    private BigDecimal giaTriGiam;
    private LocalDateTime ngayBatDau;
    private  LocalDateTime ngayKetThuc;
    

}
