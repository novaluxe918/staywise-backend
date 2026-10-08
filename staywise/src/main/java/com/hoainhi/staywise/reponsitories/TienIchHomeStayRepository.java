package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.TienIchHomeStay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TienIchHomeStayRepository extends JpaRepository<TienIchHomeStay, Long> {
}
