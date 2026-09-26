package com.hoainhi.staywise.entities;

import com.hoainhi.staywise.enums.TrangThaiPhong;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "phong")
public class Phong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maPhong")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maHomeStay", nullable = false)
    private HomeStay homeStay;

    @Column(name = "tenphong", columnDefinition = "varchar(100) ")
    private String tenPhong;

    @Column(name="loaiphong",columnDefinition = "varchar(50)")
    private String loaiPhong;

    @Column(name = "moTa")
    private String moTa;


    @Column(name = "dientich", precision = 6, scale = 2)
    private BigDecimal dienTich;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangthai", nullable = false)
    private TrangThaiPhong trangThaiPhong;

    @Column(name = "ngaytao")
    private LocalDateTime ngayTao;

    @Column(name = "daXoa")
    private Boolean daXoa;

    @OneToMany(mappedBy = "phong", fetch = FetchType.EAGER)
    private List<ChiTietGiuong> chiTietGiuongs;

    @OneToMany(mappedBy = "phong", fetch = FetchType.EAGER)
    private List<SoNguoi> soNguois;

    @OneToMany(mappedBy = "phong", fetch = FetchType.EAGER)
    private List<GiaPhong> giaPhongs;

    @OneToMany(mappedBy = "phong", fetch = FetchType.EAGER)
    private List<DuDoanGia> duDoanGias;

    @OneToMany(mappedBy = "phong", fetch = FetchType.EAGER)
    private List<DatPhong> datPhongs;


}
