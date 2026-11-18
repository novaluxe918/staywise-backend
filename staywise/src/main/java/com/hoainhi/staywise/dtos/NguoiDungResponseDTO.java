package com.hoainhi.staywise.dtos;

import com.hoainhi.staywise.enums.TrangThaiUser;
import com.hoainhi.staywise.enums.TrangThaiXacMinh;
import com.hoainhi.staywise.enums.VaiTro;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NguoiDungResponseDTO {
    private Long id;

    private String hoTen;

    private String email;

    private String soDienThoai;

    private String anhDaiDien;

    private VaiTro vaiTro;

    private TrangThaiUser trangThaiUser;

    private LocalDateTime ngayTao;

    private String anhCCCDMatTruoc;

    private String anhCCCDMatSau;

    private String anhGiayPhepKinhDoanh;

    private TrangThaiXacMinh trangThaiXacMinh;

    private String lyDoTuChoiXacMinh;
}
