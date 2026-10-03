package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.DichVu;
import com.hoainhi.staywise.enums.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DichVuRepository extends JpaRepository<DichVu, Long> {
    Page<DichVu> finByTenDichVuContainingIgnoreCase(String tuKhoa, Pageable pageable, TrangThai trangThai);
    Page<DichVu> findByTrangThaiNot(TrangThai trangThai, Pageable pageable);
}
