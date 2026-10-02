package com.hoainhi.staywise.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "khuyenMaiPhong")
public class KhuyenMaiPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maKMPhong")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "phong", nullable = false)
    private Phong phong;

    @ManyToOne
    @JoinColumn(name = "maKhuyenMai", nullable = false)
    private KhuyenMai khuyenMai;
}
