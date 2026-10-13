package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.dtos.PhongDTO;
import com.hoainhi.staywise.entities.Phong;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.enums.TrangThaiPhong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhongRepository extends JpaRepository<Phong, Long> {
    Page<Phong> findByTenPhongContainingIgnoreCase(String tuKhoa, TrangThaiPhong trangThaiPhong, Pageable pageable);
    Page<Phong> findByTrangThaiPhongNot(
            TrangThaiPhong trangThaiPhong,
            Pageable pageable
    );
}
