package com.hoainhi.staywise.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "soNguoiDat")
public class SoNguoiDat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maNguoiDat" )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maDatPhong", nullable = false)
    private DatPhong datPhong;
}
