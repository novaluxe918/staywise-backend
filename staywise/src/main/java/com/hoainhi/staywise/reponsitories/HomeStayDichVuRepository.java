package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.HomeStayDichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeStayDichVuRepository extends JpaRepository<HomeStayDichVu, Long> {
}
