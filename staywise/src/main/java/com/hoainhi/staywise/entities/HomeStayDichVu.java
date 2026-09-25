package com.hoainhi.staywise.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "homestayDichVu")
public class HomeStayDichVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maHSDichVu")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maHomeStay", nullable = false)
    private HomeStay homeStay;

    private boolean trangThai;

    @ManyToOne
    @JoinColumn(name = "maDichVu", nullable = false)
    private DichVu dichVu;
}
