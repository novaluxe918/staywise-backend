package com.hoainhi.staywise.dtos;

import com.hoainhi.staywise.enums.TrangThaiXacMinh;
import lombok.Data;

@Data
public class NguoiDungResponseDTO {
    private Long id;

    private String hoTen;

    private String email;

    private String soDienThoai;

    private String anhDaiDien;

    private String anhCCCDMatTruoc;

    private String anhCCCDMatSau;

    private String anhGiayPhepKinhDoanh;

    private TrangThaiXacMinh trangThaiXacMinh;

    private String lyDoTuChoiXacMinh;
}
