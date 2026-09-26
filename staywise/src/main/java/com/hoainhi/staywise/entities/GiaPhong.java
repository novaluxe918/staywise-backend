package com.hoainhi.staywise.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "giaPhong")
public class GiaPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maGiaPhong")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maPhong", nullable = false)
    private Phong phong;

    @Column(name = "giaPhong", precision = 15, scale = 2)
    private BigDecimal giaPhong;

    @Column(name = "ngayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngayKetThuc")
    private LocalDateTime ngayKetThuc;

}
