package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.TienIch;
import com.hoainhi.staywise.enums.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TienIchRepository extends JpaRepository<TienIch, Long> {
    Page<TienIch> findByTenTienIchContainingIgnoreCase(String tuKhoa, Pageable pageable, TrangThai trangThai);
    Page<TienIch> findByTrangThaiNot(TrangThai trangThai, Pageable pageable);
}
