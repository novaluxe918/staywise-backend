package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.HomeStay;
import com.hoainhi.staywise.entities.TienIchHomeStay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TienIchHomeStayRepository extends JpaRepository<TienIchHomeStay, Long> {
    List<TienIchHomeStay> findByHomeStay(HomeStay homeStay);
    void deleteByHomeStay_Id(Long maHomeStay);
}
