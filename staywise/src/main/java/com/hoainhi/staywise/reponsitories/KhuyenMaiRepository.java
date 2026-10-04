package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Long> {
}
