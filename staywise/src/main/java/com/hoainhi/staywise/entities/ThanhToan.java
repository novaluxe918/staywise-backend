package com.hoainhi.staywise.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hoainhi.staywise.enums.TrangThaiThanhToan;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "thanhtoan")
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maThanhToan")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maDatPhong", nullable = false)
    @JsonIgnore
    private DatPhong datPhong;

    @Column(name = "maGiaoDich")
    private String maGiaoDich;

    @Column(name = "phuongThuc")
    private String phuongThuc;

    @Column(name = "soTien", precision = 15, scale = 2)
    private BigDecimal soTien;

    @Column(name = "ngayThanhToan")
    private LocalDateTime ngayThanhToan;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangthai")
    private TrangThaiThanhToan trangThaiThanhToan;
}
