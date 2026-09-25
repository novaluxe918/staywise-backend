package com.hoainhi.staywise.entities;

import com.hoainhi.staywise.enums.TrangThaiUser;
import com.hoainhi.staywise.enums.VaiTro;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "nguoidung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maNguoiDung")
    private Long id;

    @Column(columnDefinition = "varchar(100) not null")
    private String hoTen;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String matKhau;

    @Column(columnDefinition = "varchar(10)")
    private String soDienThoai;

    @Column(columnDefinition = "varchar(10)")
    private String cccd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VaiTro vaiTro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrangThaiUser trangThaiUser;

    private String anhDaiDien;

    private LocalDateTime ngayTao;

    @OneToMany(mappedBy = "nguoiDung", fetch = FetchType.EAGER)
    private List<HomeStay> homeStays;


}
