package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.enums.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheLoaiTienIchRepository extends JpaRepository<TheLoaiTienIch, Long> {
    Page<TheLoaiTienIch> findByTenTheLoaiContainingIgnoreCase(String tuKhoa, TrangThai trangThai,  Pageable pageable);
    Page<TheLoaiTienIch> findByTrangThaiNot(TrangThai trangThai, Pageable pageable);

}
