package com.hoainhi.staywise.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class DuDoanGiaResponse {
    private Long id;

    private Long maPhong;

    private LocalDate ngayApDung;

    private BigDecimal giaDuDoan;

    private BigDecimal giaHienTai;

    private BigDecimal doTinCay;

    private String phienBan;;
}
