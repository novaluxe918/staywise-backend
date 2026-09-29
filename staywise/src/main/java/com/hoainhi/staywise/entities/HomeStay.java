package com.hoainhi.staywise.entities;

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

    @Column(name = "ngayCapNhat")
    private LocalDate ngayCapNhat;

    @Column(name = "ngayTao")
    private LocalDateTime ngayTao;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.EAGER)
    private List<HinhAnhHomeStay> homeStays;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.EAGER)
    private List<TienIchHomeStay> tienIchHomeStays;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.EAGER)
   private List<HomeStayDichVu> homeStayDichVus;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.EAGER)
    private List<Phong> phongs;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.EAGER)
    private List<DanhGia> danhGias;


}
