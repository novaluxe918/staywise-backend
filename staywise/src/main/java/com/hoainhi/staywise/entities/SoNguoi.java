package com.hoainhi.staywise.entities;

import com.hoainhi.staywise.enums.TheLoaiNguoi;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "soNguoi")
public class SoNguoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maSoNguoi")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maPhong", nullable = false)
    private Phong phong;

    @Column(name = "soluong")
    private int soLuong;

    @Enumerated(EnumType.STRING)
    @Column(name = "theloai")
    private TheLoaiNguoi theLoaiNguoi;
}
