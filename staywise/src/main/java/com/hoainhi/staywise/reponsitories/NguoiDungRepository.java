package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.enums.TrangThaiUser;
import com.hoainhi.staywise.enums.VaiTro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    boolean existsByEmail(String email);
    Optional<NguoiDung> findByEmail(String email);
    Optional<NguoiDung> findByIdAndVaiTro(Long id, VaiTro vaiTro);
    Page<NguoiDung> findByVaiTro(VaiTro vaiTro, Pageable pageable);

    // Tìm kiếm họ tên + phân trang + không phân biệt chữ hoa/thường
    Page<NguoiDung> findByVaiTroAndHoTenContainingIgnoreCase(
            VaiTro vaiTro,
            String hoTen,
            Pageable pageable
    );

    @Query("""
    SELECT nd
    FROM NguoiDung nd
    WHERE
        (
            :tuKhoa IS NULL
            OR LOWER(nd.hoTen) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))
            OR LOWER(nd.email) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))
            OR nd.soDienThoai LIKE CONCAT('%', :tuKhoa, '%')
        )
        AND (:vaiTro IS NULL OR nd.vaiTro = :vaiTro)
        AND (:trangThai IS NULL OR nd.trangThaiUser = :trangThai)
    """)
    Page<NguoiDung> timKiemNguoiDung(
            @Param("tuKhoa") String tuKhoa,
            @Param("vaiTro") VaiTro vaiTro,
            @Param("trangThai") TrangThaiUser trangThai,
            Pageable pageable
    );

}
