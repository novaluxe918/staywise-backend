package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.GiaPhong;
import com.hoainhi.staywise.enums.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GiaPhongRepository extends JpaRepository<GiaPhong, Long> {

    Page<GiaPhong> findByPhong_HomeStay_IdAndPhong_TenPhongContainingIgnoreCase(
            Long maHomeStay,
            String tuKhoa,
            Pageable pageable
    );

    @Query("""
        SELECT gp
        FROM GiaPhong gp
        WHERE gp.phong.id = :maPhong
        AND gp.trangThai = :trangThai
        AND gp.ngayBatDau <= :cuoiNgay
        AND gp.ngayKetThuc >= :dauNgay
        ORDER BY gp.ngayBatDau DESC
    """)
    List<GiaPhong> findGiaPhongTheoNgay(
            @Param("maPhong") Long maPhong,
            @Param("trangThai") TrangThai trangThai,
            @Param("dauNgay") LocalDateTime dauNgay,
            @Param("cuoiNgay") LocalDateTime cuoiNgay
    );


    List<GiaPhong> findByPhong_IdOrderByNgayBatDauAsc(Long maPhong);

    @Query("""
    SELECT gp.phong.homeStay.id, MIN(gp.giaPhong)
    FROM GiaPhong gp
    WHERE gp.phong.homeStay.id IN :danhSachMaHomeStay
    AND gp.trangThai = :trangThai
    AND gp.ngayBatDau <= :thoiDiem
    AND gp.ngayKetThuc >= :thoiDiem
    GROUP BY gp.phong.homeStay.id
""")
    List<Object[]> findGiaThapNhatTheoDanhSachHomeStay(
            @Param("danhSachMaHomeStay") List<Long> danhSachMaHomeStay,
            @Param("trangThai") TrangThai trangThai,
            @Param("thoiDiem") LocalDateTime thoiDiem
    );
}
