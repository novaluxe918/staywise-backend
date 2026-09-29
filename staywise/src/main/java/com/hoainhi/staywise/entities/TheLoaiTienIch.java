package com.hoainhi.staywise.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "theloaitienich")
public class TheLoaiTienIch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matheloai")
    private Long id;

    @Column(columnDefinition = "varchar(100)")
    private String tenTheLoai;

    private String hinhAnh;
    @OneToMany(mappedBy = "theLoaiTienIch", fetch = FetchType.EAGER)
    private List<TienIch> tienIches;


}
