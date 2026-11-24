
package com.hoainhi.staywise.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class LichSuDuDoanGiaResponse {

    private Long id;

    private Long maPhong;

    private String tenPhong;

    private String tenHomeStay;

    private LocalDateTime ngayDuDoan;

    private LocalDate ngayApDung;

    private BigDecimal giaDuDoan;

    private BigDecimal doTinCay;

    private String phienBan;
}
