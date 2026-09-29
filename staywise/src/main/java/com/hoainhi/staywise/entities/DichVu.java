package com.hoainhi.staywise.entities;

import com.hoainhi.staywise.enums.TrangThai;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "dichvu")
public class DichVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDichVu")
    private Long id;

    @Column(name = "tendichvu",columnDefinition = "varchar(100)")
    private String tenDichVu;

    @OneToMany(mappedBy = "dichVu", fetch = FetchType.EAGER)
    private List<HomeStayDichVu> homeStayDichVus;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai")
    private TrangThai trangThai;
}
