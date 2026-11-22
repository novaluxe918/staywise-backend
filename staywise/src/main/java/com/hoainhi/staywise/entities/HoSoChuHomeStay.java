package com.hoainhi.staywise.entities;

import com.hoainhi.staywise.enums.TrangThaiXacMinh;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hoso_chuhomestay")
public class HoSoChuHomeStay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maHoSo")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "maNguoiDung",
            nullable = false,
            unique = true
    )
    private NguoiDung nguoiDung;


    @Column(name = "anh_cccd_mat_truoc")
    private String anhCCCDMatTruoc;


    @Column(name = "anh_cccd_mat_sau")
    private String anhCCCDMatSau;


    @Column(name = "anh_giay_phep_kinh_doanh")
    private String anhGiayPhepKinhDoanh;


    @Enumerated(EnumType.STRING)
    @Column(name = "trangThaiXacMinh", nullable = false)
    private TrangThaiXacMinh trangThaiXacMinh;


    @Column(name = "lyDoTuChoiXacMinh", columnDefinition = "TEXT")
    private String lyDoTuChoiXacMinh;

    @Column(name = "ngayXacMinh")
    private LocalDateTime ngayXacMinh;
}
