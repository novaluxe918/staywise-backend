package com.hoainhi.staywise.entities;

import com.hoainhi.staywise.enums.TrangThai;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "loaigiuong")
public class LoaiGiuong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maLoaiGiuong")
    private Long id;

    @Column(name = "tenGiuong")
    private String tenGiuong;

    @Column(name = "hinhAnh")
    private String hinhAnh;

    @OneToMany(mappedBy = "loaiGiuong", fetch = FetchType.EAGER)
    private List<ChiTietGiuong> chiTietGiuongs;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai")
    private TrangThai trangThai;

}
