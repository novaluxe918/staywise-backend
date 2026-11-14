package com.hoainhi.staywise.dtos;

import com.hoainhi.staywise.enums.TrangThaiXacMinh;
import lombok.Data;

@Data
public class CapNhatTrangThaiXacMinhDTO {
    private TrangThaiXacMinh trangThaiXacMinh;

    private String lyDoTuChoiXacMinh;
}
