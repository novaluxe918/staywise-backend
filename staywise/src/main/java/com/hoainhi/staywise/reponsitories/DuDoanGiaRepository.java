package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.DuDoanGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DuDoanGiaRepository extends JpaRepository<DuDoanGia, Long> {
    List<DuDoanGia> findAllByOrderByNgayDuDoanDesc();
}
