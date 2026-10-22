package com.hoainhi.staywise.dtos;

import com.hoainhi.staywise.validations.ThemMoi;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GiaPhongDTO {
    private Long id;

    private Long maPhong;

    @NotNull(message = "Giá phòng không được để trống", groups = ThemMoi.class)
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Giá phòng phải lớn hơn 0")
    private BigDecimal giaPhong;

    @NotNull(message = "Ngày bắt đầu không được để trống", groups = ThemMoi.class)
    private LocalDateTime ngayBatDau;

    @NotNull(message = "Ngày kết thúc không được để trống", groups = ThemMoi.class)
    private LocalDateTime ngayKetThuc;
}
