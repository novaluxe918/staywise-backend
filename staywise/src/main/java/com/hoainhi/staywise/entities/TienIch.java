package com.hoainhi.staywise.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "tienich")
public class TienIch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matienich")
    private Long id;

    @Column(columnDefinition = "varchar(100)")
    private String tenTienIch;

    @Column(columnDefinition = "text")
    private String moTa;

    private LocalDateTime ngayTao;

    @ManyToOne
    @JoinColumn(name = "matheloai", nullable = false)
    private TheLoaiTienIch theLoaiTienIch;

    @OneToMany(mappedBy = "tienIch", fetch = FetchType.EAGER)
    private List<TienIchHomeStay> tienIchHomeStays;


}
