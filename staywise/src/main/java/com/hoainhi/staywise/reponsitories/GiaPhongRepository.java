package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.GiaPhong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiaPhongRepository extends JpaRepository<GiaPhong, Long> {

    Page<GiaPhong> findByPhong_TenPhongContainingIgnoreCase(
            String tuKhoa,
            Pageable pageable
    );
}
