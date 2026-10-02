package com.hoainhi.staywise.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "khuyenMaiHomeStay")
public class KhuyenMaiHomeStay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maKMHomeStay")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maHomeStay", nullable = false)
    private HomeStay homeStay;

    @ManyToOne
    @JoinColumn(name = "maKhuyenMai", nullable = false)
    private KhuyenMai khuyenMai;

}
