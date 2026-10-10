package com.hoainhi.staywise.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hoainhi.staywise.enums.TheLoaiHomeStay;
import com.hoainhi.staywise.enums.TrangThaiHomeStay;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "homestay")
public class HomeStay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maHomeStay")
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "maChuHomeStay", nullable = false)
    private NguoiDung nguoiDung;

    @Column(name = "tenHomeStay",columnDefinition = "varchar(100) not null")
    private String tenHomeStay;

    @Column(name = "moTa",columnDefinition = " text")
    private String moTa;

    @Column(name = "phuongXa",nullable = false)
    private String phuongXa;

    @Column(name = "quanHuyen",nullable = false)
    private String quanHuyen;

    @Column(name = "tinhThanh",nullable = false)
    private String tinhThanh;

    @Column(name="diaChi",nullable = false)
    private String diaChi;

    @Column(name = "sodienthoai",columnDefinition = " varchar(10)")
    private String soDienThoai;

    @Column(columnDefinition = "varchar(100)")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangthai",nullable = false)
   private TrangThaiHomeStay trangThaiHomeStay;

    @Column(name = "lyDoTuChoi",columnDefinition = "text")
    private String lyDoTuChoi;

    @Column(name = "ngayDangKy")
    private LocalDate ngayDangKy;

    @Column(name = "ngayPheDuyet")
    private LocalDate ngayPheDuyet;

    @Enumerated(EnumType.STRING)
    @Column(name = "theloai")
    private TheLoaiHomeStay theLoaiHomeStay;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.LAZY)
    private List<HinhAnhHomeStay> homeStays;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.LAZY)
    private List<TienIchHomeStay> tienIchHomeStays;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.LAZY)
   private List<HomeStayDichVu> homeStayDichVus;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.LAZY)
    private List<Phong> phongs;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.LAZY)
    private List<DanhGia> danhGias;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.LAZY)
    private List<KhuyenMaiHomeStay> khuyenMaiHomeStays;

}
