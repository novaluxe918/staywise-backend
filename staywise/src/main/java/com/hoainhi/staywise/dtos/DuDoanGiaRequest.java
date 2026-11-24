package com.hoainhi.staywise.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DuDoanGiaRequest {

    private Long maPhong;

    @NotNull(message = "Ngày áp dụng không được để trống")
    @FutureOrPresent(message = "Ngày áp dụng phải từ hôm nay")
    private LocalDate ngayApDung;
}
