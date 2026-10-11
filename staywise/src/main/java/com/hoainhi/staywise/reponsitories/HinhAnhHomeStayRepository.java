package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.HinhAnhHomeStay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HinhAnhHomeStayRepository extends JpaRepository<HinhAnhHomeStay, Long> {
    List<HinhAnhHomeStay> findByHomeStay_Id(Long maHomeStay);
}
