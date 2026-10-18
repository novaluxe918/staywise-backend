package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.HomeStay;
import com.hoainhi.staywise.enums.TrangThaiHomeStay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeStayRepository extends JpaRepository<HomeStay, Long> {
   Page<HomeStay> findByTenHomeStayContaining(String tuKhoa, Pageable pageable);
   Page<HomeStay> findByTrangThaiHomeStayNot(TrangThaiHomeStay trangThaiHomeStay, Pageable pageable);

}
