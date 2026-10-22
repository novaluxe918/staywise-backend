package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.SoNguoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoNguoiRepository extends JpaRepository<SoNguoi, Long> {
    List<SoNguoi> findByPhong_Id(Long maPhong);
}
