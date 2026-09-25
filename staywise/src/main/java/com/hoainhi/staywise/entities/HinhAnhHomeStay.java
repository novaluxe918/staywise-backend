package com.hoainhi.staywise.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "hinhanhHomeStay")
public class HinhAnhHomeStay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maHinhAnh")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maHomeStay", nullable = false)
    private HomeStay homeStay;

    private String duongDan;

    private boolean laAnhDaiDien;

    private LocalDateTime ngayTao;

}
