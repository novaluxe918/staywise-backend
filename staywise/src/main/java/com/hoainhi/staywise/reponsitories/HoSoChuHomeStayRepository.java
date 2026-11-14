package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.HoSoChuHomeStay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HoSoChuHomeStayRepository
        extends JpaRepository<HoSoChuHomeStay, Long> {


    Optional<HoSoChuHomeStay> findByNguoiDung_Id(Long maNguoiDung);


    boolean existsByNguoiDung_Id(Long maNguoiDung);
}