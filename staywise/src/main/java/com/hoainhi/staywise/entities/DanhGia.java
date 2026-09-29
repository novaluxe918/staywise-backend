package com.hoainhi.staywise.entities;

import com.hoainhi.staywise.enums.TrangThaiDanhGia;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "danhgia")
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDanhGia")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maNguoiDung", nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "maHomeStay", nullable = false)
    private HomeStay homeStay;

    @ManyToOne
    @JoinColumn(name = "maDatPhong", nullable = false)
    private DatPhong datPhong;

    @Column(name = "soSao")
    private int soSao;

    @Column(name = "noiDung", columnDefinition = " text")
    private String noiDung;

    @Column(name = "ngayDanhGia")
    private LocalDateTime ngayDanhGia;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai")
    private TrangThaiDanhGia trangThaiDanhGia;




}
