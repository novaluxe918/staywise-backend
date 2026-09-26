package com.hoainhi.staywise.entities;

import com.hoainhi.staywise.enums.TheLoaiNguoi;
import com.hoainhi.staywise.enums.TrangThaiDatPhong;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "datPhong")
public class DatPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDatPhong")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maNguoiDung", nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "maPhong", nullable = false)
    private Phong phong;

    @Column(name = "ngaynhanphong")
    private LocalDateTime ngayNhanPhong;

    @Column(name = "ngayTraPhong")
    private LocalDateTime ngayTraPhong;

    @Column(name = "ngayDat")
    private LocalDateTime ngayDat;

    @Column(name = "tong_tien", precision = 15, scale = 2, nullable = false)
    private BigDecimal tongTien;

    @Column(name = "ghiChu")
    private String ghiChu;

    @Column(name = "ngayCapNhat")
    private LocalDateTime ngayCapNhat;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangthai")
   private TrangThaiDatPhong trangThaiDatPhong;

    @OneToMany(mappedBy = "datPhong", fetch = FetchType.EAGER)
    private List<SoNguoiDat> soNguoiDats;

    @Enumerated(EnumType.STRING)
    @Column(name = "loaiNguoi")
    private TheLoaiNguoi theLoaiNguoi;

    @OneToMany(mappedBy = "datPhong", fetch = FetchType.EAGER)
    private List<ThanhToan> thanhToans;

    @OneToMany(mappedBy = "datPhong", fetch = FetchType.EAGER)
    private List<DanhGia> danhGias;

}
