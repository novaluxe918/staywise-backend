package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.ChiTietGiuong;
import com.hoainhi.staywise.entities.Phong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietGiuongRepository extends JpaRepository<ChiTietGiuong, Long> {
    void deleteByPhong(Phong phong);
}
