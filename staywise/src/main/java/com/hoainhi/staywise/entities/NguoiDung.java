package com.hoainhi.staywise.entities;

import com.hoainhi.staywise.enums.TrangThaiUser;
import com.hoainhi.staywise.enums.VaiTro;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "nguoidung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maNguoiDung")
    private Long id;

    @Column(name="hoten",columnDefinition = "varchar(100) not null")
    private String hoTen;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "matKhau",nullable = false)
    private String matKhau;

    @Column(name = "soDienThoai",columnDefinition = "varchar(10)")
    private String soDienThoai;

    @Column(name = "cccd",columnDefinition = "varchar(10)")
    private String cccd;

    @Enumerated(EnumType.STRING)
    @Column(name = "vaiTro",nullable = false)
    private VaiTro vaiTro;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangthai", nullable = false)
    private TrangThaiUser trangThaiUser;

    @Column(name = "anhDaiDien")
    private String anhDaiDien;

    @Column(name = "ngaytao")
    private LocalDateTime ngayTao;

    @OneToMany(mappedBy = "nguoiDung", fetch = FetchType.EAGER)
    private List<HomeStay> homeStays;

    @OneToMany(mappedBy = "nguoiDung", fetch = FetchType.EAGER)
    private List<DatPhong> datPhongs;

    @OneToMany(mappedBy = "nguoiDung", fetch = FetchType.EAGER)
    private List<DanhGia> danhGias;
}
