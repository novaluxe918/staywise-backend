package com.hoainhi.staywise.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DuDoanGiaAIResponse {

    private Long maPhong;

    private LocalDate ngayApDung;

    private BigDecimal giaDuDoan;

    private BigDecimal doTinCay;

    private String phienBan;
}