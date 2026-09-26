package com.hoainhi.staywise.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "chitietgiuong")
public class ChiTietGiuong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machitiet")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maLoaiGiuong", nullable = false)
    private LoaiGiuong loaiGiuong;

    @ManyToOne
    @JoinColumn(name = "maPhong", nullable = false)
    private Phong phong;

    @Column(name = "soluong")
    private int soLuong;


}
