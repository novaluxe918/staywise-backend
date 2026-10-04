package com.hoainhi.staywise.entities;

import com.hoainhi.staywise.enums.LoaiGiam;
import com.hoainhi.staywise.enums.TrangThaiKhuyenMai;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "khuyenmai")
public class KhuyenMai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maKhuyenMai")
    private Long id;

    @Column(name = "tenKhuyenMai")
    private String tenKhuyenMai;

    @Enumerated(EnumType.STRING)
    @Column(name = "loaiGiam")
    private LoaiGiam loaiGiam;

    @Column(name = "giaTriGiam", precision = 15, scale = 2)
    private BigDecimal giaTriGiam;

    @Column(name = "ngayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngayKetThuc")
    private  LocalDateTime ngayKetThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai")
    private TrangThaiKhuyenMai trangThaiKhuyenMai;

    @OneToMany(mappedBy = "khuyenMai", fetch = FetchType.EAGER)
    private List<KhuyenMaiHomeStay> khuyenMaiHomeStays;

    @OneToMany(mappedBy = "khuyenMai", fetch = FetchType.EAGER)
    private List<KhuyenMaiPhong> khuyenMaiPhongs;

}
