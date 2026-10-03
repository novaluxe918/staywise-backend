package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.LoaiGiuong;
import com.hoainhi.staywise.enums.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface LoaiGiuongRepository extends JpaRepository<LoaiGiuong, Long> {
    Page<LoaiGiuong> findByTenGiuongContainingIgnoreCase(String tuKhoa, TrangThai trangThai, Pageable pageable);
    Page<LoaiGiuong> findByTrangThaiNot(TrangThai trangThai, Pageable pageable);

}
