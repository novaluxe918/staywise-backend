package com.hoainhi.staywise.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "duDoanGia")
public class DuDoanGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDuDoan")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maPhong", nullable = false)
    private Phong phong;

    @Column(name = "ngayDuDoan")
    private LocalDateTime ngayDuDoan;

    @Column(name = "giaDuDoan", precision = 15, scale = 2)
    private BigDecimal giaDuDoan;

    @Column(name = "doTinCay", precision = 5, scale = 2)
    private BigDecimal doTinCay;

    @Column(name = "phienban")
    private String phienBan;

    @Column(name = "ngayTao")
    private LocalDateTime ngayTao;
}
