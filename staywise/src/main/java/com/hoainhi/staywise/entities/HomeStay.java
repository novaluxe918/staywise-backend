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

    @Column(columnDefinition = "varchar(100) not null")
    private String tenHomeStay;

    @Column(columnDefinition = " text")
    private String moTa;

    @Column(nullable = false)
    private String phuongXa;

    @Column(nullable = false)
    private String quanHuyen;

    @Column(nullable = false)
    private String tinhThanh;

    @Column(nullable = false)
    private String diaChi;

    @Column(columnDefinition = " varchar(10)")
    private String soDienThoai;

    @Column(columnDefinition = "varchar(100)")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
   private TrangThaiHomeStay trangThaiHomeStay;

    @Column(columnDefinition = "text")
    private String lyDoTuChoi;

    private LocalDate ngayDangKy;

    private LocalDate ngayPheDuyet;

    private LocalDate ngayCapNhat;

    private LocalDateTime ngayTao;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.EAGER)
    private List<HinhAnhHomeStay> homeStays;

    private boolean daXoa;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.EAGER)
    private List<TienIchHomeStay> tienIchHomeStays;

    @OneToMany(mappedBy = "homeStay", fetch = FetchType.EAGER)
   private List<HomeStayDichVu> homeStayDichVus;



}
